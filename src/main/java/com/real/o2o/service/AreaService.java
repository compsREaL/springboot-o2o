package com.real.o2o.service;

import com.real.o2o.entity.Area;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/12 18:32
 */
public interface AreaService {

    public static final String AREALISTKEY = "arealist";

    List<Area> getAreaList();
}
