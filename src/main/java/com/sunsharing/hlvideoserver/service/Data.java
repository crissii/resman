package com.sunsharing.hlvideoserver.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.eos.common.utils.StringUtils;
import com.sunsharing.hlvideoserver.sys.BMUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by criss on 14/12/23.
 */
@Service
@Transactional
public class Data {

    Logger logger = Logger.getLogger(Data.class);

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    BMUtils bm;
    public Map query(Scheme scheme,int page,int rows,Map columns)
    {
        String where = " where 1=1 ";
        Set<String> ck = columns.keySet();
        for(String column:ck)
        {
            Object o = columns.get(column);
            if(o instanceof String && StringUtils.isBlank((String)o))
            {
                continue;
            }
            if(o instanceof String[] && o!=null && ((String[])o).length==0)
            {
                continue;
            }
            if(column.endsWith("_begin"))
            {
                where += " and "+column.substring(0,column.length()-6)+" >= '"+columns.get(column)+"' ";
            }else
            if(column.endsWith("_end"))
            {
                where += " and "+column.substring(0,column.length()-4)+" <= '"+columns.get(column)+"' ";
            }else if(columns.get(column) instanceof String[])
            {
                String[] v = (String[])columns.get(column);
                if(v!=null && v.length>0)
                {
                    String val = "'";
                    for(int i=0;i<v.length;i++)
                    {
                        val+= v[i]+"','";
                    }
                    val = val.substring(0,val.length()-2);
                    where += " and "+column+ " in("+val+") ";
                }

            }else
            {
                where += " and "+column+" like '%"+columns.get(column)+"%' ";
            }

        }
        int begin = (page-1)*rows;
        String sql = "select * from "+scheme.getTable()+" "+where+" limit "+begin+","+rows;
        logger.info(sql);
        String sql2 = "select count(*) from "+scheme.getTable()+" "+where;
        int count = jdbc.queryForInt(sql2);
        List<Map<String,Object>> list =(List<Map<String,Object>>) jdbc.queryForList(sql);
        for(Map row:list)
        {
            transRow(row,scheme.getAllColumns(),false);
        }
        Map rst = new HashMap();
        rst.put("rows",list);
        rst.put("total",count);
        return rst;
    }

    public void delete(String table,String idcolumn,String id)
    {
        String sql = "delete from "+table+" where "+idcolumn+"="+id;
        jdbc.execute(sql);
    }

    public void add(Scheme scheme,Map obj)
    {
        Set<String> keys = obj.keySet();
        String table = scheme.getTable();
        String idcolumn = scheme.getIDColumn();
        if(keys.size()==0)
        {
            return;
        }
        String sql = "";
        if(StringUtils.isBlank((String) obj.get(idcolumn)))
        {
            sql = "insert into "+table+" set ";
        }else
        {
            sql = "update  "+table+" set ";
        }
        for(String column : keys)
        {
            String v = (String)obj.get(column);
            if(!StringUtils.isBlank(v))
            {
                sql+=column+" = '"+v+"',";
            }
        }
        sql = sql.substring(0,sql.length()-1);
        if(!StringUtils.isBlank((String)obj.get(idcolumn)))
        {
            sql += " where "+idcolumn+" = "+(String)obj.get(idcolumn);
        }

        logger.info("sql:"+sql);
        jdbc.execute(sql);
    }

    private void transRow(Map row,JSONArray allColumns,boolean detail)
    {
        //JSONArray array = scheme.getJSONArray("columns");
        for(int i=0;i<allColumns.size();i++)
        {
            JSONObject column = allColumns.getJSONObject(i);
            String type = column.getString("type");
            if(!detail)
            {
                if("bm".equals(type))
                {
                    String field = column.getString("field");
                    String tbl = column.getString("tbl");
                    String value = (String)row.get(field);
                    if(!StringUtils.isBlank(value))
                    {
                        value = bm.getData(tbl,value);
                        row.put(field, value);
                    }
                }
            }
            if("vcolumn".equals(type))
            {
                String expression = column.getString("value");
                String field = column.getString("field");
                List<Variable> variables = new ArrayList<Variable>();
                Set<String> colums = row.keySet();
                for(String co : colums)
                {
                    variables.add(Variable.createVariable(co, row.get(co))); //执行表达式
                }
                Object result = ExpressionEvaluator.evaluate(expression,
                        variables);
                if(result!=null)
                {
                    row.put(field,result.toString());
                }else
                {
                    row.put(field,"");
                }
            }
        }
    }

    @Autowired
    BMUtils bmUtils;

    public void saveData(final List datas,String schemejs,final Map columnIndex,final int dataIndex,boolean isDelete)
    {
        Scheme scheme = new Scheme(schemejs);
        //JSONObject scheme = Scheme.loadScheme(schemejs);
        String table = scheme.getTable();
        if(isDelete)
        {
            jdbc.execute("delete from "+table);
        }

        final JSONArray columns = scheme.getDBColumns();
        String setColumn = "";
        for(int i=0;i<columns.size();i++)
        {
            JSONObject column = columns.getJSONObject(i);
            String dbcolumn = column.getString("field");
            String ci = (String)columnIndex.get(dbcolumn);
            if(!StringUtils.isBlank(ci))
            {
                int index = new Integer(ci);
                setColumn+=dbcolumn+"=?,";
            }
        }
        setColumn = setColumn.substring(0,setColumn.length()-1);
        String sql = "insert into "+table+" set "+setColumn;
        jdbc.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setLong(1, i + 10);
//                        ps.setString(2, firstNames.get(i));
//                        ps.setString(3, lastNames.get(i));
//                        ps.setNull(4, Types.TIMESTAMP);
//                        ps.setNull(5, Types.CLOB);
                        List row = (List)datas.get(i+dataIndex);
                        int columnnum = 0;
                        for(int j=0;j<columns.size();j++)
                        {
                            JSONObject column = columns.getJSONObject(j);
                            String dbcolumn = column.getString("field");
                            String type = column.getString("type");
                            String tbl = column.getString("tbl");
                            String ci = (String)columnIndex.get(dbcolumn);
                            if(!StringUtils.isBlank(ci))
                            {
                                columnnum++;
                                int index = new Integer(ci);
                                String data = (String)row.get(index);
                                //处理Type Date Type BM
                                if(StringUtils.isBlank(data))
                                {
                                    ps.setString(columnnum,"");
                                }else
                                {
                                    if("bm".equals(type))
                                    {
                                        bmUtils.insertBM(tbl,data);
                                        data = bmUtils.getData(tbl,data);
                                        if(StringUtils.isBlank(data))
                                        {
                                            data = "";
                                        }
                                    }
                                    if("date".equals(type))
                                    {
                                        data = DateUtils.getDBString(data);
                                        data = DateUtils.getDisplay(data).substring(0,10);
                                    }
                                    ps.setString(columnnum, data);
                                }
                            }
                        }


                    }
                    public int getBatchSize() {
                        return datas.size()-dataIndex;
                    }
                });

    }



}
