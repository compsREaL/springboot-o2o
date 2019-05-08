package com.real.o2o.service;

import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ShopExecution;
import com.real.o2o.entity.Shop;

/**
 * @author: mabin
 * @create: 2019/4/13 10:59
 */
public interface ShopService {

    /**
     * 根据shopCondition分页相应的所有数据
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
    /**
     * 通过shopId获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);
    /**
     * 添加店铺
     * @param shop
     * @param imageHolder
     * @return
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder);

    /**
     * 更新店铺信息
     * @param shop
     * @param imageHolder
     * @return
     */
    ShopExecution modifyShop(Shop shop, ImageHolder imageHolder);


}
