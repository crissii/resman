<script id="importDlg" type="text/html">
    <div id="importDlgDiv" class="easyui-dialog" style="width:600px;height:100%;padding:10px 20px"
         closed="true" buttons="#import-buttons">
        <form id="excelupload" action="importUploadFile.do" method="post" enctype="multipart/form-data" target="fileform">
            <div class="fitem">
                <label>选择Excel:</label>
                <input class="easyui-filebox" style="width: 200px"  name="file" data-options="prompt:'Choose a file...',onChange:changeFile">
            </div>
        </form>

        <hr />
        <form id="excelRow" method="post">
        <div class="fitem:">
                <label>是否删除原有数据:</label>
                <input type="checkbox" name="deletedata" value="1"/>
            </div>
        <div class="fitem">
            <label>读取第几页:</label>
            <select class="easyui-combobox" id="sheetnum" data-options="valueField:'id',textField:'text',panelHeight:'auto'"  name="sheetnum" style="width: 150px">
            </select>
        </div>
        <div class="fitem">
            <label>标题位于:</label>
            <select class="easyui-combobox" id="titelnum" data-options="panelHeight:'auto'" name="titelnum" style="width: 150px">
                <option value="0">第一行</option>
                <option value="1">第二行</option>
                <option value="2">第三行</option>
            </select>
            <a href="#" class="easyui-linkbutton" onclick="loadColumns()" data-options="iconCls:'icon-reload'" style="width:80px">自动匹配</a>
        </div>
        {{each columns as value i}}
        {{if value.type!="vcolumn"}}
        <div class="fitem">
            <label>{{value.label}}:</label>
            <select class="easyui-combobox js-excelcolumn" id="{{value.field}}" name="{{value.field}}"
                    data-options="panelHeight:'auto',valueField:'id',textField:'text'" style="width: 150px"></select>
        </div>
        {{/if}}
        {{/each}}
        </form>
    </div>
    <div id="import-buttons">
        <a href="javascript:void(0)" id="saveimport" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveData()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#importDlgDiv').dialog('close')" style="width:90px">Cancel</a>
    </div>
    <iframe id="fileform" name="fileform" style="display: none" />

</script>
<script>
    function changeFile()
    {
        var v = $('#excelupload input[name="file"]').val();
        if(v.indexOf("xls")==-1 && v.indexOf("xlsx")==-1)
        {
            //alert("请选择Excel");
        }else
        {
            $('#excelupload').submit();
        }
    }

    var filename = "";
    function addSuccess(f)
    {
        filename = f;
        getSheets();

    }
    function importDlg()
    {
        $('#importDlgDiv').dialog('open').dialog('setTitle','导入');
        $("#importDlgDiv").panel("move",{top:15,left:200});
        $('#excelupload').form('clear');
        $('#excelRow').form('clear');
    }
    function getSheets()
    {
        $('#sheetnum').combobox('reload', 'getSheets.do?filename='+filename)
    }

    function loadColumns()
    {
        var sheetnum = ($("#sheetnum").combobox("getValue"));
        var titlenum = ($("#titelnum").combobox("getValue"));
        if(sheetnum==""){
            alert("请选择Excel文件的第几页");
            return;
        }
        if(titlenum==""){
            alert("请选择Excel文件的标题行");
            return;
        }
        $.ajax({
            type: 'POST',
            url: "getColumns.do",
            dataType:"json",
            data: "filename="+filename+"&sheetnum="+sheetnum+"&titilenum="+titlenum+"&schemejs="+schemejs,
            success: function(data){
                $(".js-excelcolumn").combobox("loadData",data["excelColumns"]);
                var column2Excel = data["column2Excel"];
                for(var key in column2Excel)
                {
                    $("#excelRow #"+key).combobox("select",column2Excel[key]+"");
                }
            }
        });
    }

    function saveData()
    {
        $("#saveimport").linkbutton("disable");
        var data = $("#excelRow").serialize();
        data+="&filename="+filename+"&schemejs="+schemejs;
        $.ajax({
            type: 'POST',
            url: "importExcel.do",
            dataType:"json",
            data: data,
            success: function(data){
                location.reload();
            }
        });
    }


</script>