package com.sunsharing.hlvideoserver.plugin;

import com.sunsharing.eos.common.utils.StringUtils;
import com.sunsharing.hlvideoserver.service.AbsDataInterface;
import com.sunsharing.hlvideoserver.service.Scheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by criss on 15/7/10.
 */
@Service
@Transactional
public class JyjfService extends AbsDataInterface {

    @Autowired
    JdbcTemplate jdbc;

    public Map query(Scheme scheme,int page,int rows,Map columns)
    {
        String sfzh = (String)columns.get("SFZH");
        String where = " where 1=1 ";
        if(!StringUtils.isBlank(sfzh))
        {
            where += " AND SFZH='"+sfzh+"' ";
        }
        String name = (String)columns.get("NAME");
        if(!StringUtils.isBlank(name))
        {
            where += " AND NAME='"+name+"' ";
        }

        String regID = (String)columns.get("REG_ID");
        if(!StringUtils.isBlank(regID))
        {
            where += " AND REG_ID='"+regID+"' ";
        }
        where+=" limit 10 ";


        String sql = "SELECT REG_ID,SFZH,NAME FROM T_PREREG_STUDENT "+where+" union \n" +
                " SELECT REG_ID,SFZH,NAME FROM T_PREREG_GUARDIAN "+where;

        System.out.println(sql);
        List list = jdbc.queryForList(sql);
        Map rst = new HashMap();
        rst.put("rows",list);
        rst.put("total",list.size());
        return rst;
    }

}
