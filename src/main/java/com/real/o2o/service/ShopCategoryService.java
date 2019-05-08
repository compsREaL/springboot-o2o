package com.real.o2o.service;

import com.real.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/16 10:49
 */
public interface ShopCategoryService {

    public static final String SHOPCATEGORYLISTKEYS = "shopcategorylist";

    /**
     * 根据查询条件获取商铺类别列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
