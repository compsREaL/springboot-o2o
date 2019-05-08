package com.real.o2o.service;

import com.real.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 13:41
 */
public interface ProductSellDailyService {

    /**
     * 每日定时对所有商铺的商品销量进行统计
     */
    void dailyCalculate();

    /**
     * 感觉查询条件返回商品日销售的统计列表
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,Date endTime);
}
