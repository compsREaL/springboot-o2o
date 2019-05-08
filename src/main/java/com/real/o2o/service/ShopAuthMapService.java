package com.real.o2o.service;

import com.real.o2o.dto.ShopAuthMapExecution;
import com.real.o2o.entity.ShopAuthMap;
import com.real.o2o.exception.ShopAuthMapOperationException;

/**
 * @author: mabin
 * @create: 2019/5/6 17:02
 */
public interface ShopAuthMapService {

    /**
     * 根据店铺id分页显示该店铺的授权信息
     * @param shopId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);

    /**
     * 根据shopAuthId返回对应的授权信息
     * @param shopAuthId
     * @return
     */
    ShopAuthMap getShopAuthMapById(Long shopAuthId);

    /**
     * 添加授权信息
     * @param shopAuthMap
     * @return
     * @throws ShopAuthMapOperationException
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

    /**
     * 更新授权信息
     * @param shopAuthMap
     * @return
     * @throws ShopAuthMapOperationException
     */
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;
}
