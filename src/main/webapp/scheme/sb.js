var data = {
    "id":"sb",
    "title":"社保录入",
    "table" : "T_HAND_SAFT",
    "key" : "ID",
    "plugin":{
        "saveService":"sbService"
    },
    "columns":[
        {
            "label" : "身份证号",
            "field":"SFZH",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true
        },
        {
            "label" : "姓名",
            "field":"NAME",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true
        },
        {
            "label" : "缴费年月",
            "field":"NYUE00",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true
        },
        {
            "label" : "建账年月",
            "field":"JZNYUE",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true,
            "value":"NYUE00"
        },
        {
            "label" : "数据来源",
            "field":"SJLY",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true,
            "default":"医保"
        },
        {
            "label" : "单位名称",
            "field":"DWMC00",
            "listwidth" :"80",
            "type": "text"
        },
        {
            "label" : "缴费月数",
            "field":"JFYS00",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true,
            "default":"1"
        },
        {
            "label" : "证明类型",
            "field":"ZMLX00",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true,
            "default":"01"
        }
    ]
};