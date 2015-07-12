package com.sunsharing.hlvideoserver.express;

import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.eos.common.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by criss on 15/7/6.
 */
public class MyExpress {

    public  String substring(String str,Integer start,Integer end)
    {
        if(StringUtils.isBlank(str))
        {
            return "";
        }
        return str.substring(start,end);
    }

    public  String getCurrentTime()
    {
        return DateUtils.getDBString(new Date());
    }

    public  int string2int(String str)
    {
        if(StringUtils.isBlank(str))
        {
            return 0;
        }else
        {
            return new Integer(str);
        }
    }

    public int getYear(String date)
    {
        if(StringUtils.isBlank(date))
        {
            return 0;
        }else
        {
            return new Integer(date.substring(0,4));
        }
    }

    public boolean isBlank(String str)
    {
        return StringUtils.isBlank(str);
    }

    public int monthDiff(String date1,String date2)
    {
        if(StringUtils.isBlank(date1) || StringUtils.isBlank(date2))
        {
            return 0;
        }

        date1 = DateUtils.getDBString(date1);
        date2 = DateUtils.getDBString(date2);
        Date d1 = DateUtils.getStringToDate(date1);
        Date d2 = DateUtils.getStringToDate(date2);
        Calendar calbegin = Calendar.getInstance();
        Calendar calend = Calendar.getInstance();
        calbegin.setTime(d1);
        calend.setTime(d2);
        int m_begin = calbegin.get(Calendar.MONTH)+1; //获得合同开始日期月份
        int m_end = calend.get(Calendar.MONTH)+1;
        //获得合同结束日期月份
        int checkmonth = m_end-m_begin+(calend.get(Calendar.YEAR)-calbegin.get(Calendar.YEAR))*12;
        //获得合同结束日期于开始的相差月份
        return checkmonth;
    }

}
