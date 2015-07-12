package com.sunsharing.hlvideoserver.web.controller.main;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.hlvideoserver.excel.ExcelImport;
import com.sunsharing.hlvideoserver.service.Data;
import com.sunsharing.hlvideoserver.service.DataInterface;
import com.sunsharing.hlvideoserver.service.Scheme;
import com.sunsharing.hlvideoserver.sys.BMUtils;
import com.sunsharing.hlvideoserver.sys.ServiceLocator;
import com.sunsharing.hlvideoserver.web.common.ResponseHelper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunsharing.hlvideoserver.web.common.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController extends BaseController {

    @Autowired
    BMUtils bmUtils;

	/**
	 * 用户登陆页面
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delete.do")
    public void delete(Model model,HttpServletRequest request,HttpServletResponse res) throws Exception {
        String schemejs = request.getParameter("schemejs");
        Scheme scheme = new Scheme(schemejs);
        String table = scheme.getTable();
        String id = request.getParameter(scheme.getIDColumn());
        data.delete(table,scheme.getIDColumn(),id);
        ResponseHelper.printOut(res, "true");
    }

    @Autowired
    Data data;
    @RequestMapping(value="/query.do")
    public void list(int page,int rows,String schemejs,HttpServletResponse res,HttpServletRequest req){
        Map hasMap = req.getParameterMap();
        Scheme scheme = new Scheme(schemejs);

        Set<String> keys = hasMap.keySet();
        Map columns = new HashMap();
        for(String key :keys)
        {
            String [] val = (String[])hasMap.get(key);
            if(val!=null && val.length==1)
            {
                columns.put(key,val[0]);
            }else
            {
                if(key.endsWith("[]"))
                {
                    key = key.substring(0,key.length()-2);
                }
                columns.put(key,val);
            }
        }
        columns.remove("schemejs");
        columns.remove("page");
        columns.remove("rows");

        String table = scheme.getTable();
        Map datas = null;
        //JSONObject scheme = Scheme.loadScheme(schemejs);
        if(!StringUtils.isBlank(scheme.getQueryService()))
        {
            DataInterface di = (DataInterface)ServiceLocator.getBean(scheme.getQueryService());
            datas = di.query(scheme,page,rows,columns);
        }else
        {
            datas = data.query(scheme,page,rows,columns);
        }
        String rst = JSONObject.toJSONString(datas);
        ResponseHelper.printOut(res, rst);
    }
    @RequestMapping(value="/loadbm.do")
    public void loadbm(String tbl,HttpServletRequest req,HttpServletResponse res)
    {
        List list = bmUtils.getTblData(tbl);
        String rst = JSONArray.toJSONString(list);
        ResponseHelper.printOut(res, rst);
    }


    @RequestMapping(value="/add.do")
    public void add(HttpServletRequest req,HttpServletResponse res){

        String schemejs = req.getParameter("schemejs");
        Scheme scheme = new Scheme(schemejs);
        Map hasMap = req.getParameterMap();

        Set<String> keys = hasMap.keySet();
        Map columns = new HashMap();
        for(String key :keys)
        {
            String [] val = (String[])hasMap.get(key);
            if(val!=null && val.length==1 && !"schemejs".equals(key))
            {
                columns.put(key,val[0]);
            }
        }

        List<Variable> variables = new ArrayList<Variable>();
        Set<String> colums = columns.keySet();
        for(String co : colums)
        {
            variables.add(Variable.createVariable(co, columns.get(co))); //执行表达式
        }


        JSONArray array = scheme.getDBColumns();
        for(int i=0;i<array.size();i++)
        {
            JSONObject obj = (JSONObject)array.get(i);
            if(obj.getString("value")!=null)
            {
                Object result = ExpressionEvaluator.evaluate(obj.getString("value"),
                        variables);
                if(result!=null)
                {
                    columns.put(obj.getString("field"),result.toString());
                }
            }
        }

        String table = scheme.getTable();
        if(!StringUtils.isBlank(scheme.getSaveService()) &&
               StringUtils.isBlank((String) columns.get(scheme.getIDColumn())) )
        {
            //保存走接口
            DataInterface di = (DataInterface)ServiceLocator.getBean(scheme.getSaveService());
            di.add(scheme, columns);
        }else
        {
            data.add(scheme, columns);
        }
        if(columns.get(scheme.getIDColumn())!=null)
        {
            ResponseHelper.printOut(res, JSONObject.toJSONString(columns));
        }else
        {
            ResponseHelper.printOut(res, "true");
        }

    }

    @RequestMapping(value="/importUploadFile", method=RequestMethod.POST)
    public void addUser(String tilerow, @RequestParam MultipartFile file, HttpServletRequest request,HttpServletResponse res)
            throws IOException {
        //如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解
        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解
        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件
        if(file!=null){
            if(file.isEmpty()){
                System.out.println("文件未上传");
            }else{
                System.out.println("文件长度: " + file.getSize());
                System.out.println("文件类型: " + file.getContentType());
                System.out.println("文件名称: " + file.getName());
                System.out.println("文件原名: " + file.getOriginalFilename());
                System.out.println("========================================");
                //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
                String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
                String ext = file.getOriginalFilename().split("\\.")[1];
                String uuid = StringUtils.generateUUID();
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, uuid+"."+ext));

                String rst = "<script>parent.addSuccess('"+uuid+"."+ext+"')</script>";
                ResponseHelper.printOut(res, rst);

            }
        }
        //return "";
    }

    @RequestMapping(value="/getSheets")
    public void getSheets(String filename,HttpServletRequest request,HttpServletResponse res)
    {
        String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
        File path = new File(realPath, filename);
        ExcelImport excelImport = new ExcelImport();
        List sheetlist = excelImport.getSheetNums(path);
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("id","");
        obj.put("text","请选择");
        array.add(obj);
        for(int i=0;i<sheetlist.size();i++)
        {
            String name = (String)sheetlist.get(i);
            obj = new JSONObject();
            obj.put("id",i+"");
            obj.put("text",name);
            array.add(obj);
        }
        logger.info(array.toJSONString());
        ResponseHelper.printOut(res,array.toJSONString());
    }

    @RequestMapping(value="/getColumns")
    public void getColumns(String schemejs,String filename,int sheetnum,int titilenum,HttpServletRequest request,
                           HttpServletResponse res)
    {
        Scheme scheme = new Scheme(schemejs);
        ExcelImport excelImport = new ExcelImport();
        String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
        File path = new File(realPath, filename);
        List rows = excelImport.readExcel(path,sheetnum);
        Map rst = new HashMap();
        List<String> excelColumn = (List<String>)rows.get(titilenum);
        List<Map> excelMapColumn = new ArrayList<Map>();
        Map head = new HashMap();
        head.put("id","");
        head.put("text","请选择");
        excelMapColumn.add(head);
        int row = 0;
        for(String excelName:excelColumn)
        {
            Map v = new HashMap();
            v.put("id",row+"");
            v.put("text",excelName);
            excelMapColumn.add(v);
            row++;
        }
        rst.put("excelColumns",excelMapColumn);
        Map column2Excel = new HashMap();
        rst.put("column2Excel",column2Excel);
        JSONArray columns = scheme.getAllColumns();
        for(int i=0;i<columns.size();i++)
        {
            JSONObject obj = columns.getJSONObject(i);
            String field = obj.getString("field");
            String label = obj.getString("label");
            int index = 0;
            for(String excelName:excelColumn)
            {
                if(excelName.equals(label))
                {
                    column2Excel.put(field,index);
                    break;
                }
                index++;
            }
        }
        ResponseHelper.printOut(res,JSONObject.toJSONString(rst));
    }

    @RequestMapping(value="/importExcel")
    public void importExcel(String schemejs,String filename,int sheetnum,int titelnum,
                            HttpServletRequest request, HttpServletResponse res)
    {
        String deletedata = request.getParameter("deletedata");
        boolean delete = false;
//        if("1".equals(deletedata))
//        {
//            delete = true;
//        }
        Scheme scheme = new Scheme(schemejs);
        JSONArray columns = scheme.getDBColumns();
        Map columnIndex = new HashMap();
        for(int i=0;i<columns.size();i++)
        {
            JSONObject column = columns.getJSONObject(i);
            String field = (String)column.get("field");
            String value = request.getParameter(field);
            if(!StringUtils.isBlank(value))
            {
                columnIndex.put(field,value+"");
            }
        }
        int dataIndex = titelnum+1;
        ExcelImport excelImport = new ExcelImport();
        String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
        File path = new File(realPath, filename);
        List rows = excelImport.readExcel(path,sheetnum);
        data.saveData(rows,schemejs,columnIndex,dataIndex,delete);
        ResponseHelper.printOut(res,"true");
    }


}
