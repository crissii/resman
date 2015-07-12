<script id="configDlg" type="text/html">
    <div id="configDlgDiv" class="easyui-dialog" style="width:600px;height:500px;padding:10px 20px"
         closed="true" buttons="#config-buttons">
        <div class="ftitle">定制列表</div>
        <p>查询条件&nbsp;<input id="conALlcheck" onclick="clickAll(this,'searchColumn')" type="checkbox" /></p>
        <div style="margin:20px 0"></div>
        <ul style="position: relative;margin:0;padding:0;margin-left:10px;">
            {{each allSearchColumn as value i}}
            {{if value.searchcolumn!=false}}
            <li class="drag-item"><input name="searchColumn" {{value.checked}} value="{{value.field}}" type="checkbox" />&nbsp;{{value.label}}&nbsp;
                <a onclick="movedown(this)" href="javascript:void(0)" style="float: right;font-size: 16px" class="typcn typcn-arrow-down-thick"></a>
                <a onclick="moveup(this)" href="javascript:void(0)" style="float: right;font-size: 16px;margin-right: 10px" class="typcn typcn-arrow-up-thick"></a></li>
            {{/if}}
            {{/each}}

        </ul>
        <p>简要列表&nbsp;<input id="listALlcheck" onclick="clickAll(this,'listColumn')" type="checkbox" /></p>
        <div style="margin:20px 0"></div>
        <ul style="position: relative;margin:0;padding:0;margin-left:10px;">
            {{each allSimpleColumn as value i}}
            <li class="drag-item"><input name="listColumn" {{value.checked}} value="{{value.field}}" type="checkbox" />&nbsp;{{value.label}}&nbsp;
                <a onclick="movedown(this)" href="javascript:void(0)" style="float: right;font-size: 16px" class="typcn typcn-arrow-down-thick"></a>
                <a onclick="moveup(this)" href="javascript:void(0)" style="float: right;font-size: 16px;margin-right: 10px" class="typcn typcn-arrow-up-thick"></a></li>
            {{/each}}
        </ul>

    </div>
    <div id="config-buttons">
        <a href="javascript:void(0)" id="configsave" onclick = "saveConfig()" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#configDlgDiv').dialog('close')" style="width:90px">Cancel</a>
    </div>
</script>
<style type="text/css">
    .drag-item{
        list-style-type:none;
        display:block;
        padding:5px;
        border:1px solid #ccc;
        margin:2px;
        width:300px;
        background:#fafafa;
        color:#444;
        display: block;
    }
    .indicator{
        position:absolute;
        font-size:9px;
        width:10px;
        height:10px;
        display:block;
        color:red;
    }
</style>
<script>
    function clickAll(obj,column)
    {
        var checked = $(obj).prop("checked");
        $("input[name='"+column+"']").prop("checked",checked);
    }

    function moveup(source)
    {

        var li = $(source).parent();
        li.parent().children().css("background-color","#fafafa");
        li.parent().children().children("a").css("background-color","#fafafa");
        li.css("background-color","#ffe789");
        li.children("a").css('background-color',"#ffe789");
        var index = li.index();
        if(index==0)
        {
            return;
        }
        li.insertBefore(li.prev());
    }
    function movedown(source)
    {

        var li = $(source).parent();
        li.parent().children().css("background-color","#fafafa");
        li.parent().children().children("a").css("background-color","#fafafa");
        li.css('background-color',"#ffe789");
        li.children("a").css('background-color',"#ffe789");
        var index = li.index();
        var len = li.parent().children().length;
        if(index==len-1)
        {
            return;
        }
        li.insertAfter(li.next());
    }

    function saveConfig()
    {
        $("#configsave").linkbutton("disable");
        var searchSelect = [];
        $("input[name='searchColumn']").each(
                function (o)
                {
                    if($(this).prop("checked"))
                    {
                        searchSelect[searchSelect.length] = $(this).val();
                    }
                }
        );
        var simpleSelect = [];
        $("input[name='listColumn']").each(
                function (o)
                {
                    if($(this).prop("checked"))
                    {
                        simpleSelect[simpleSelect.length] = $(this).val();
                    }
                }
        );
        $.cookie(data.id+'searchSelect', searchSelect.join(","));
        $.cookie(data.id+'simpleSelect', simpleSelect.join(","));
        location.reload();
    }
    //定制列表
    function config()
    {
        $('#configDlgDiv').dialog('open').dialog('setTitle','定制');
        $("#configDlgDiv").panel("move",{top:10,left:200});
    }

</script>