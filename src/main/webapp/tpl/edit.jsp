<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunsharing.hlvideoserver.service.Scheme" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.alibaba.fastjson.JSONArray" %>
<%@ page import="com.sunsharing.hlvideoserver.sys.BMUtils" %>
<%@ page import="com.sunsharing.hlvideoserver.sys.ServiceLocator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.sunsharing.eos.common.utils.StringUtils" %>
<form id="editfm{{index}}" method="post" novalidate>
            <table class="dv-table" style="width:600px;border:1px solid #ccc;padding:5px;margin-top:5px;"  cellspacing="5">
                <%
                    BMUtils bmUtils = (BMUtils) ServiceLocator.getBeanByClass(BMUtils.class);
                    String schemeJs = request.getParameter("scheme");
                    Scheme scheme = new Scheme(schemeJs);
                    JSONArray columns = scheme.getAllColumns();
                    for(int i=0;i<columns.size();i++)
                    {
                        JSONObject row = columns.getJSONObject(i);
                        if(i%2==0)
                        {
                            out.println("<tr>");
                        }
                        out.println("<td><label>"+row.get("label")+":</label></td>");
                        String type = (String)row.get("type");
                        if(type.equals("text"))
                        {
                            out.println("<td><input class=\"easyui-textbox\" name=\""+row.get("field")+"\" " +
                                    "style=\"width:"+row.getString("width")+"\">&nbsp;&nbsp;</td>");
                        }
                        if(type.equals("date"))
                        {
                            out.println("<td><input class=\"easyui-datebox\"  data-options=\"formatter:myformatter,parser:myparser\" " +
                                    "name=\""+row.get("field")+"\" " +
                                    "style=\"width:110px\">&nbsp;&nbsp;</td>");
                        }
                        if(type.equals("bm"))
                        {
                            out.println("<td><select data-options=\"panelHeight:'auto'\" class=\"easyui-combobox\" name=\""+row.get("field")+"\" style=\"width:200px;\">\n");
                            out.println("<option value=\"\">请选择</option>\n");
                            String tbl = (String)row.get("tbl");
                            List data = bmUtils.getTblData(tbl);
                            for(int j=0;j<data.size();j++)
                            {
                                Map bmrow = (Map)data.get(j);
                                out.println("<option value=\""+bmrow.get("BH0000")+"\">"+bmrow.get("MC0000")+"</option>\n");
                            }
                            out.println("</select></td>");
                        }
                        if(type.equals("vcolumn"))
                        {
                            out.println("<td><input name='"+row.get("field")+"' disabled readonly/></td>");
                        }
                        if (i%2==1)
                        {
                            out.println("</tr>");
                        }
                    }
                    if (columns.size()%2==1)
                    {
                        out.println("</tr>");
                    }
                %>

    </table>
    </form>
    <div id="dlg-buttons" style="width: 600px;padding:5px 0;text-align:right;padding-right:0px">
        <%
            if(!StringUtils.isBlank(scheme.getTable()))
            {
        %>
            <a href="#" class="easyui-linkbutton" onclick="saveItem(<%=request.getParameter("index")%>)" iconCls="icon-save" plain="true">Save</a>&nbsp;&nbsp;
        <%
            }
        %>
    </div>