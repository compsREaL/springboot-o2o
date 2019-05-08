package com.real.o2o.service;

import com.real.o2o.dto.UserProductMapExecution;
import com.real.o2o.entity.UserProductMap;
import com.real.o2o.exception.UserProductMapOperationException;

/**
 * @author: mabin
 * @create: 2019/5/7 14:38
 */
public interface UserProductMapService {

    /**
     * 通过传入的条件分页列出用户消费信息列表
     * @param userProductCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserProductMapExecution listUserProductMap(UserProductMap userProductCondition,Integer pageIndex,Integer pageSize);

    /**
     * 添加消费记录
     * @param userProductMap
     * @return
     * @throws UserProductMapOperationException
     */
    UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;
}
