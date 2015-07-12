package com.sunsharing.hlvideoserver.sys;

import com.sunsharing.eos.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by criss on 15/6/30.
 */
@Service
@Transactional
public class BMUtils {

    @Autowired
    JdbcTemplate jdbc;

    Map<String,Map<String,String>> dataMap = new HashMap();

    @PostConstruct
    public void reloadDatas()
    {
        String sql = "select BH0000,MC0000 from BM_TABLE";
        List tals = jdbc.queryForList(sql);
        sql = "select BH0000,MC0000,TBL from BM_DATA";
        List datas =  jdbc.queryForList(sql);
        for(Iterator iter = tals.iterator();iter.hasNext();)
        {
            Map tabeRow = (Map)iter.next();
            String bh0000 = (String)tabeRow.get("BH0000");
            Map<String,String> data = new HashMap<String,String>();
            for(Iterator iter2 = datas.iterator();iter2.hasNext();)
            {
                Map dataRow =  (Map)iter2.next();
                String tbl = (String)dataRow.get("TBL");
                String dataKey = (String)dataRow.get("BH0000");
                String dataLabel = (String)dataRow.get("MC0000");
                if(tbl.equals(bh0000))
                {
                    data.put(dataKey,dataLabel);
                }
            }
            dataMap.put(bh0000,data);
        }
    }

    public void reloadTbl(String table)
    {
        String sql = "select BH0000,MC0000,TBL from BM_DATA where TBL='"+table+"'";
        List data = jdbc.queryForList(sql);
        Map<String,String> data2 = new HashMap<String,String>();
        for(Iterator iter = data.iterator();iter.hasNext();)
        {
            Map dataRow =  (Map)iter.next();
            String dataKey = (String)dataRow.get("BH0000");
            String dataLabel = (String)dataRow.get("MC0000");
            data2.put(dataKey,dataLabel);
        }
        dataMap.put(table,data2);
    }

    public String getKey(String tbl,String data)
    {
        return getResData(tbl,data,true);
    }

    private String getKeyOrData(String table,String v,boolean getData)
    {
        Map das = dataMap.get(table);
        if(!getData)
        {
            if(das!=null)
            {
                String row = (String)das.get(v);
                return row;
            }else
            {
                return null;
            }
        }else
        {
            if(das!=null)
            {
                Set<String> s = das.keySet();
                for(String key:s)
                {
                    String data = (String)das.get(key);
                    if(v.equals(data))
                    {
                        return key;
                    }
                }
                //String row = (String)das.get(v);
                return v;
            }else
            {
                return null;
            }
        }
    }

    private String getResData(String table,String v,boolean getData)
    {
        Map das = dataMap.get(table);
        if(das!=null)
        {
            String row = getKeyOrData(table,v,getData);
            if(StringUtils.isBlank(row))
            {
                reloadTbl(table);
                row = getKeyOrData(table,v,getData);
            }
            if(StringUtils.isBlank(row))
            {
                return v;
            }
            return row;
        }else
        {
            reloadTbl(table);
            das = dataMap.get(table);
            if(das==null)
            {
                return v;
            }
            String row = getKeyOrData(table,v,getData);
            if(StringUtils.isBlank(row))
            {
                return v;
            }
            return row;
        }
    }

    public String getData(String table,String key)
    {
        return getResData(table,key,false);
    }

    public List getTblData(String tbl)
    {
        Map das = dataMap.get(tbl);
        if(das==null)
        {
            return new ArrayList();
        }
        Set keys = das.keySet();
        List keysList = new ArrayList(keys);
        Collections.sort(keysList);
        List rst = new ArrayList();
        for(Iterator iter = keysList.iterator();iter.hasNext();)
        {
            Map row = new HashMap();
            String key = (String)iter.next();
            String value = (String)das.get(key);
            row.put("BH0000",key);
            row.put("MC0000",value);
            rst.add(row);
        }
        return rst;
    }

    public void insertBM(String tbl,String data)
    {
        String sql = "select BH0000,MC0000 from BM_TABLE where BH0000='"+tbl+"'";
        List tals = jdbc.queryForList(sql);
        if(tals.size()==0)
        {
            sql = "insert into BM_TABLE set BH0000='"+tbl+"',MC0000='"+tbl+"'";
            jdbc.execute(sql);
        }
        sql = "select BH0000,MC0000,TBL from BM_DATA where TBL='"+tbl+"' and MC0000='"+data+"'";
        List datas = jdbc.queryForList(sql);
        if(datas.size()==0)
        {
            sql = "insert into BM_DATA set BH0000='"+data+"',MC0000='"+data+"',TBL='"+tbl+"'";
            jdbc.execute(sql);
        }
    }



}
