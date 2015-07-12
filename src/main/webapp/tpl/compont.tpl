<script id="date_range" type="text/html">
     <input class="easyui-datebox" name="{{field}}_begin" data-options="formatter:myformatter,parser:myparser" style="width:110px">
    To: <input class="easyui-datebox" name="{{field}}_end" data-options="formatter:myformatter,parser:myparser" style="width:110px">&nbsp;&nbsp;
</script>
<script id="text" type="text/html">
     <input class="easyui-textbox js_form_{{field}}" name="{{field}}" style="width:{{width}}">&nbsp;&nbsp;
</script>
<script id="date" type="text/html">
    <input class="easyui-datebox" name="{{field}}"
           data-options="formatter:myformatter,parser:myparser" style="width:150px">&nbsp;&nbsp;
</script>
<script id="bm" type="text/html">
    <input class="easyui-combobox js-{{field}}" id="{{field}}"  name="{{field}}"
           data-options="valueField:'BH0000',textField:'MC0000',panelHeight:'auto'">&nbsp;&nbsp;
</script>
<script id="bm_sear" type="text/html">
    <input class="easyui-combobox js-{{field}}" id="{{field}}"  name="{{field}}"
           data-options="valueField:'BH0000',textField:'MC0000',multiple:true,panelHeight:'auto'">&nbsp;&nbsp;
</script>

        <script type="text/javascript">
            function myformatter(date){
                var y = date.getFullYear();
                var m = date.getMonth()+1;
                var d = date.getDate();
                return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
            }

            function myparser(s){
                if (!s) return new Date();
                var ss = (s.split('-'));
                var y = parseInt(ss[0],10);
                var m = parseInt(ss[1],10);
                var d = parseInt(ss[2],10);
                if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
                    return new Date(y,m-1,d);
                } else {
                    return new Date();
                }
            }
        </script>
