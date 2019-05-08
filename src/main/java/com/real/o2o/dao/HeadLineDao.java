package com.real.o2o.dao;

import com.real.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/29 15:32
 */
public interface HeadLineDao {

    /**
     * 根据传入的条件查询
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);


}
