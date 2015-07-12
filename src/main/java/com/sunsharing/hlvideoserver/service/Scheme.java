package com.sunsharing.hlvideoserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.hlvideoserver.sys.SysInit;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;

/**
 * Created by criss on 15/6/28.
 */
public class Scheme {
    public Scheme(String schemeJs)
    {
        loadScheme(schemeJs);
    }

    JSONObject jsonObject = null;

    public  void loadScheme(String scheme)
    {
        String path = SysInit.sysPath;
        path = path.replaceAll("\\\\","/");
        if(!path.endsWith("/"))
        {
            path += "/";
        }
        FileInputStream in = null;
        try
        {
            in = new FileInputStream(path+"scheme/"+scheme);
            String content = (IOUtils.toString(in,"UTF-8")).trim();
            int i = content.indexOf("=");
            content = content.substring(i+1,content.length()-1);
            jsonObject = JSONObject.parseObject(content);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(in);
        }
    }

    public  String getTable()
    {
        if(jsonObject == null)
        {
            throw new RuntimeException("scheme 还没有初始化");
        }
        return (String)jsonObject.get("table");
    }

    public String getIDColumn()
    {
        return (String)jsonObject.get("key");
    }

    public JSONArray getDBColumns()
    {
        JSONArray array = (JSONArray)jsonObject.getJSONArray("columns");
        JSONArray rst = new JSONArray();
        for(int i=0;i<array.size();i++)
        {
            JSONObject row = array.getJSONObject(i);
            if(!row.getString("type").equals("vcolumn"))
            {
                rst.add(row);
            }
        }
        return rst;
    }
    public JSONArray getAllColumns()
    {
        return jsonObject.getJSONArray("columns");
    }

    public JSONObject getColumn(String fileld)
    {
        JSONArray array = getAllColumns();
        for(int i=0;i<array.size();i++)
        {
            JSONObject obj = (JSONObject)array.get(i);
            if(obj.getString("field").equals(fileld))
            {
                return obj;
            }
        }
        return null;
    }
    public String getSaveService()
    {
        JSONObject plugin = (JSONObject)jsonObject.getJSONObject("plugin");
        if(plugin==null)
        {
            return null;
        }else
        {
            return plugin.getString("saveService");
        }

    }

    public String getQueryService()
    {
        JSONObject plugin = (JSONObject)jsonObject.getJSONObject("plugin");
        if(plugin==null)
        {
            return null;
        }else
        {
            return plugin.getString("searchService");
        }
    }

}
