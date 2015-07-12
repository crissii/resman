function initData()
{
    //初始化查询条件
    var columns = data.columns;

    var id = data.id;
    var o = initSelect(id+"searchSelect",columns);

    data.mySearchColumn = o.select;
    data.allSearchColumn = o.allcolumn;

    o = initSelect(id+"simpleSelect",columns);
    data.mySimpleList = o.select;
    data.allSimpleColumn = o.allcolumn;

}

function initSelect(cookieName,columns)
{
    var id = data.id;
    var searchSelect = $.cookie(cookieName);
    var mySearchColumn = [];
    var allSearchColumn = [];
    if(searchSelect!="" && searchSelect!=null && searchSelect!=undefined)
    {
        console.info(searchSelect);
        var tmp = searchSelect.split(",");
        for(var i=0;i<tmp.length;i++)
        {
            var t = getColumn(columns,tmp[i]);
            if(t!=null)
            {
                t.checked = "checked";
                t.simplelist = true;
                mySearchColumn[mySearchColumn.length] = t;
                allSearchColumn[allSearchColumn.length] = t;
            }
        }
        for(var i=0;i<columns.length;i++)
        {
            var f = columns[i].field;
            if(searchSelect.indexOf(f)==-1)
            {
                var oo = columns[i];
                var rst = new Object();
                for(var key in oo)
                {
                    rst[key] = oo[key];
                }
                rst.checked = "";
                allSearchColumn[allSearchColumn.length] = rst;
            }
        }

    }else
    {
        if(cookieName==id+"simpleSelect")
        {
            for(var i=0;i<columns.length;i++)
            {
                var s = columns[i].simplelist;
                if(s)
                {
                    mySearchColumn[mySearchColumn.length] = columns[i];
                }
            }
        }
        allSearchColumn = columns;
    }

    return {"allcolumn":allSearchColumn,"select":mySearchColumn}
}

function getColumn(columns,field)
{
    for(var j=0;j<columns.length;j++)
    {
        if(columns[j].field == field)
        {
            var oo = columns[j];
            var rst = new Object();
            for(var key in oo)
            {
                rst[key] = oo[key];
            }
            return rst;
        }
    }
    return null;
}