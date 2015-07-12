package com.sunsharing.hlvideoserver.plugin;

import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.eos.common.utils.StringUtils;
import com.sunsharing.hlvideoserver.service.AbsDataInterface;
import com.sunsharing.hlvideoserver.service.Data;
import com.sunsharing.hlvideoserver.service.DataInterface;
import com.sunsharing.hlvideoserver.service.Scheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * Created by criss on 15/7/10.
 */
@Service
@Transactional
public class SbService extends AbsDataInterface {

    @Autowired
    Data data;

    public void add(Scheme scheme,Map obj){

        String nyue = (String)obj.get("NYUE00");
        String reg = "";
        if(nyue.indexOf("-")!=-1)
        {
            reg = "-";
        }if(nyue.indexOf("~")!=-1)
        {
            reg = "~";
        }if(nyue.indexOf(",")!=-1)
        {
            reg = ",";
        }
        if(StringUtils.isBlank(reg))
        {
            data.add(scheme,obj);
        }else
        {
            String[] array = nyue.split(reg);
            String begin = array[0];
            String end = array[1];
            if(begin.compareTo(end)>0)
            {
                throw new RuntimeException("起始时间大于结束时间");
            }
            Date beginDate = DateUtils.getStringToDate(begin);
            Date endDate = DateUtils.getStringToDate(end);
            while(true)
            {

                if(DateUtils.getDBString(beginDate).substring(0,6).compareTo(DateUtils.getDBString(endDate).substring(0,6))<=0)
                {
                    obj.put("NYUE00",DateUtils.getDBString(beginDate).substring(0,6));
                    obj.put("JZNYUE",DateUtils.getDBString(beginDate).substring(0,6));
                    data.add(scheme,obj);
                }else
                {
                    break;
                }
                beginDate = DateUtils.addMonth(beginDate,1);
            }
        }

    }


}
