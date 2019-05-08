package com.real.o2o.service;

import com.real.o2o.dto.UserShopMapExecution;
import com.real.o2o.entity.UserShopMap;

/**
 * @author: mabin
 * @create: 2019/5/7 18:43
 */
public interface UserShopMapService {

    /**
     * 根据传入的查询信息分页查询用户积分列表
     * @param userShopMapCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserShopMapExecution listUserShopMapList(UserShopMap userShopMapCondition,int pageIndex,int pageSize);

    /**
     * 根据用户id和店铺id返回该用户在某个店铺的积分情况
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap getUserShopMap(Long userId,Long shopId);

}
