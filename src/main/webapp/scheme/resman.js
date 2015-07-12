var data = {
    "id":"resman",
    "title":"员工花名册",
    "table" : "T_WORKERS",
    "key" : "id",
    "columns":[
        {
            "label" : "部门",
            "field":"dept",
            "listwidth" :"80",
            "type": "bm",
            "tbl":"BM_DEPT",
            "simplelist" : true
        },
        {
            "label" : "姓名",
            "field":"name",
            "listwidth" :"80",
            "type": "text",
            "simplelist" : true
        },
        {
            "label" : "职务",
            "field" : "duty",
            "type": "bm",
            "tbl":"BM_DUTY"
        },
        {
            "label" : "实习开始时间",
            "field" : "study_start",
            "listwidth" :"80",
            "type": "date"
        },
        {
            "label" : "入职时间",
            "field" : "work_time",
            "listwidth" :"80",
            "type": "date",
            "simplelist" : true
        },
        {
            "label" : "拟转正时间",
            "field" : "want_regular_time",
            "listwidth" :"80",
            "type": "date",
            "simplelist" : true
        },
        {
            "label" : "实际转正时间",
            "field" : "real_regular_time",
            "listwidth" :"80",
            "type": "date"
        },
        {
            "label" : "合同签订",
            "field" : "contract_time",
            "listwidth" :"80",
            "type": "date"
        },
        {
            "label" : "合同终止时间",
            "field" : "contract_end_time",
            "listwidth" :"80",
            "type": "date"
        },
        {
            "label" : "入职年数",
            "field" : "rzns",
            "type": "vcolumn",
            "value":"$isBlank(work_time)?0:($getYear($getCurrentTime())-$getYear(work_time))",
            "searchcolumn":false,
            "simplelist" : true
        },
        {
            "label" : "入职月份",
            "field" : "rzyf",
            "type": "vcolumn",
            "value":"$monthDiff(work_time,$getCurrentTime()) % 12",
            "sqlvalue":"",
            "searchcolumn":false
        },
        {
            "label" : "人员状态",
            "field" : "status",
            "listwidth":"80",
            "type": "text"

        },
        {
            "label" : "归属地",
            "field" : "address",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "身份证号",
            "field" : "sfzh",
            "listwidth":"80",
            "type": "text",
            "simplelist" : true
        },
        {
            "label" : "出生日期",
            "field" : "birthday",
            "listwidth":"80",
            "type": "date",
            "simplelist" : true
        },
        {
            "label" : "性别",
            "field" : "sex",
            "listwidth":"80",
            "type": "bm",
            "tbl":"BM_SEX",
            "simplelist" : true
        },
        {
            "label" : "婚姻状况",
            "field" : "hy",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "户口所在地",
            "field" : "accouter",
            "listwidth":"150",
            "type": "text"
        },
        {
            "label" : "户口",
            "field" : "hk",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "户口性质",
            "field" : "hkxz",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "毕业年份",
            "field" : "edu_year",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "工作年数",
            "field" : "work_year",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "毕业院校",
            "field" : "edu_school",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "学历",
            "field" : "education",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "专业",
            "field" : "zy",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "职称",
            "field" : "zc",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "归属项目",
            "field" : "gsxm",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "当月入职",
            "field" : "dyrz",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "巨龙入职时间",
            "field" : "dragon_time",
            "listwidth":"80",
            "type": "text"
        },
        {
            "label" : "备注",
            "field" : "bz",
            "listwidth":"80",
            "type": "text"
        }


    ]
};