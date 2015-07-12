<%@ page import="com.sunsharing.eos.common.utils.StringUtils" %>
<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: criss
  Date: 15/6/27
  Time: 下午8:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%
        String schemejs = request.getParameter("scheme");
        if(StringUtils.isBlank(schemejs))
        {
            schemejs = "resman.js";
        }
        long d = new Date().getTime();
    %>
    <script type="text/javascript" src="scheme/<%=schemejs%>?d=<%=d%>"></script>
    <link rel="stylesheet" type="text/css" href="lib/jquery-easyui-1.4.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="lib/jquery-easyui-1.4.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="lib/jquery-easyui-1.4.2/themes/color.css">
    <link rel='stylesheet' href='lib/typicons.font-master/src/font/typicons.min.css' />
    <script type="text/javascript" src="lib/jquery-easyui-1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="lib/jquery.cookie.js"></script>
    <script type="text/javascript" src="lib/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="lib/jquery-easyui-1.4.2/datagrid-detailview.js"></script>
    <script type="text/javascript" src="lib/artTemplate/dist/template.js"></script>
    <script type="text/javascript" src="index.js"></script>
    <style>
        #fm{
            margin:0;
            padding:10px 10px;
        }
        .ftitle{
            font-size:14px;
            font-weight:bold;
            padding:5px 0;
            margin-bottom:10px;
            border-bottom:1px solid #ccc;
        }
        .fitem{
            margin-bottom:5px;
        }
        .fitem label{
            display:inline-block;
            width:90px;
            margin-left: 10px;
        }
        .fitem input{
            width:150px;
        }

        form{
            margin:0;
            padding:0;
        }
        .dv-table td{
            border:0;
            font-size: 12px;
        }
        .dv-table input{
            border:1px solid #ccc;
        }

    </style>
</head>
<body style="overflow: hidden">
<script type="text/javascript">
    $.fn.serializeObject = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
</script>
<div id="content"></div>
<!--选择框控件-->
<jsp:include page="tpl/compont.tpl"/>
<jsp:include page="tpl/add.tpl"/>
<jsp:include page="tpl/config.tpl"/>
<jsp:include page="tpl/import.tpl"/>

<script id="contentScript" type="text/html">
    <table id="dg"  title="{{title}}"
           toolbar='#tb'
            footer='#ft'
            rownumbers=true
            singleSelect=true
            autoRowHeight=false
            pagination=true
            pageSize=10
            rownumbers =true
            singleSelect =true
           style="width:100%;height:480px"
           >
        <thead>
        <tr>
            {{each mySimpleList as value i}}
                {{if value.simplelist==true}}
                    <th data-options="field:'{{value.field}}',width:'{{value.listwidth}}'">{{value.label}}</th>
                {{/if}}
            {{/each}}
        </tr>
        </thead>
    </table>
    <div id="tb" style="padding:2px 5px;">
        <form id="tbform" method="post" >
        {{each mySearchColumn as value i}}
            {{each compontent_types as type i}}
                {{if value.type==type}}
                    {{if type=='date'}}
                    {{value.label}}:{{include 'date_range' value}}
                    {{/if}}
                    {{if type=='bm'}}
                    {{value.label}}:{{include 'bm_sear' value}}
                    {{/if}}
                    {{if type!='date' && type!='bm'}}
                    {{value.label}}:{{include type value}}
                    {{/if}}

                {{/if}}
            {{/each}}
        {{/each}}
            <a href="#" class="easyui-linkbutton"  onclick="seach()" iconCls="icon-search">Search</a>
            <a href="#" class="easyui-linkbutton" onclick="config()" iconCls="icon-lock">定制</a>
            <a href="#" class="easyui-linkbutton" onclick="importDlg()" iconCls="icon-reload">导入</a>

        </form>
    </div>
    {{if table!=""}}
    <div id="ft" style="padding:2px 5px;">
        <a href="#" class="easyui-linkbutton" onclick="add()" iconCls="icon-add" plain="true"></a>
        <a href="#" class="easyui-linkbutton" onclick="del()" iconCls="icon-remove" plain="true"></a>
    </div>
    {{/if}}
    {{include 'addDlg'}}
    {{include 'configDlg'}}
    {{include 'importDlg'}}
</script>

<script type="text/javascript">
    function isvisual(column)
    {
        if(column.type=="vcolumn")
        {
            return true;
        }else
        {
            return false;
        }
    }
    function isAutoAdd(column)
    {
        if(column.type!="vcolumn" && column.value!=null)
        {
            return true;
        }else
        {
            return false;
        }
    }
    var schemejs = '<%=schemejs%>';
    data["compontent_types"] = ["date_range","text","date","bm"];
    initData();
    template.helper('isvisual', isvisual);
    template.helper('isAutoAdd', isAutoAdd);
    var html = template('contentScript', data);
    document.getElementById('content').innerHTML = html;
</script>
<script>
    //打开添加窗口
    function seach()
    {
        var obj = $("#tbform").serializeObject();
        $('#dg').datagrid('load',obj);
    }

    function del()
    {
        var row = $('#dg').datagrid('getSelected');
        if (row){
            var id = row[data.key];
            $.ajax({
                type: 'POST',
                url: "delete.do",
                data: data.key+'='+id+"&schemejs=<%=schemejs%>",
                success: function(){
                    seach();
                }
            });
        }
    }
    //更新
    function saveItem(index){
        var row = $('#dg').datagrid('getRows')[index];
        var url = 'add.do?'+data.key+'='+row[data.key]+"&schemejs=<%=schemejs%>";
        $('#dg').datagrid('getRowDetail',index).find('form').form('submit',{
            url: url,
            onSubmit: function(){
                return $(this).form('validate');
            },
            success: function(data){
                var obj = $("#tbform").serializeObject();
                $('#dg').datagrid('reload',obj);
            }
        });
    }
    function loadBM()
    {
        for(var i=0;i<data.columns.length;i++)
        {
            if(data.columns[i].type=="bm")
            {
                $(".js-"+data.columns[i]["field"]).combobox('reload', 'loadbm.do?tbl='+data.columns[i]["tbl"]);
            }
        }
    }

</script>
<script>
    $('#dg').datagrid({
        url:'query.do?schemejs=<%=schemejs%>',
        method:'post',
        view: detailview,
        detailFormatter:function(index,row){
            return '<div class="ddv"></div>';
        },
        onExpandRow: function(index,row){
            var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
            ddv.panel({
                border:false,
                cache:true,
                href:'tpl/edit.jsp?index='+index+"&scheme=<%=schemejs%>",
                onLoad:function(){
                    $('#dg').datagrid('fixDetailRowHeight',index);
                    $('#dg').datagrid('selectRow',index);
                    $('#dg').datagrid('getRowDetail',index).find('form').form('load',row);
                }
            });
            $('#dg').datagrid('fixDetailRowHeight',index);
        }
    });
    setTimeout('loadBM()',1000);
</script>
</body>
</html>
