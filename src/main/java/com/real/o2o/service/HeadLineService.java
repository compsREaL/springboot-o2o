package com.real.o2o.service;

import com.real.o2o.entity.HeadLine;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/29 15:48
 */
public interface HeadLineService {

    public static String HEADLINELISTKEY = "headlinelist";

    /**
     * 根据查询的条件返回指定的头条列表
     * @param headLineCondition
     * @return
     */
    List<HeadLine> headLineList(HeadLine headLineCondition);
}
