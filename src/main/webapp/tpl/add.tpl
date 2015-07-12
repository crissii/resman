<script id="addDlg" type="text/html">
<div id="dlg" class="easyui-dialog" style="width:700px;height:500px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <div class="ftitle">{{title}}</div>
    <form id="fm" method="post" novalidate>
        {{each columns as value i}}
            {{if i%2==0}}
            <div class="fitem">
            {{/if}}
                {{if isvisual(value)==false && isAutoAdd(value)==false}}
                <label>{{value.label}}:</label>
                {{each compontent_types as type i}}
                    {{if value.type==type}}
                        {{include type value}}
                    {{/if}}
                {{/each}}
                {{/if}}
            {{if i%2==1}}
            </div>
            {{/if}}
        {{/each}}
        {{if columns.length%2==0}}
            </div>
        {{/if}}
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveOk('{{table}}')" style="width:90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">Cancel</a>
</div>
</script>
<script>
    function saveOk(table)
    {
        $.ajax({
            type: 'POST',
            dataType :"json",
            url: "add.do",
            data: $('#fm').serialize()+"&schemejs="+schemejs,
            success: function(data){
                if(data.status!=null && data.status==false)
                {
                    console.info(data);
                    alert("保存异常，请联系管理员");
                    return;
                }
                $('#dlg').dialog('close');        // close the dialog
                $('#dg').datagrid('reload');    // reload the user data
            }
        });
    }
    function add(){
        $('#dlg').dialog('open').dialog('setTitle','添加');
        $("#dlg").panel("move",{top:15,left:200});
        $('#fm').form('clear');
        setDefault();
    }
    function setDefault()
    {
        var columns = data.columns;
        for(var i=0;i<columns.length;i++)
        {
            var column = columns[i];
            //alert(column.default);
            if(column.type=="text")
            {
                if(column.default!=null)
                {
                    $("#fm .js_form_"+column["field"]).textbox("setValue",column.default);
                }
                if(column.edit_readonly)
                {
                    $("#fm .js_form_"+column["field"]).textbox("readonly");
                }
            }
        }
    }

</script>