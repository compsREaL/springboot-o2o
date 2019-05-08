package com.real.o2o.service;

import com.real.o2o.dto.UserAwardMapExecution;
import com.real.o2o.entity.UserAwardMap;
import com.real.o2o.exception.UserAwardMapOperationException;

/**
 * @author: mabin
 * @create: 2019/5/7 19:33
 */
public interface UserAwardMapService {

    /**
     * 根据条件分页获取映射列表及总数
     * @param userAwardMapCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserAwardMapExecution getUserAwardMapList(UserAwardMap userAwardMapCondition,Integer pageIndex,Integer pageSize);

    /**
     * 根据userAwardId获取映射信息
     * @param userAwardId
     * @return
     */
    UserAwardMap getUserAwardMapById(Long userAwardId);

    /**
     * 领取奖品，添加映射信息
     * @param userAwardMap
     * @return
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

    /**
     * 修改映射信息，主要对奖品的领取信息进行修改
     * @param userAwardMap
     * @return
     * @throws UserAwardMapOperationException
     */
    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;


}
