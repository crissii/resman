package com.sunsharing.hlvideoserver.service;

import java.util.Map;

/**
 * Created by criss on 15/7/10.
 */
public interface DataInterface {

    public void add(Scheme scheme,Map obj);

    public Map query(Scheme scheme,int page,int rows,Map columns);

}
