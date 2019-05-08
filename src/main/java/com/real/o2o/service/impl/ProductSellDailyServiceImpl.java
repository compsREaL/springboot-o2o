package com.real.o2o.service.impl;

import com.real.o2o.dao.ProductSellDailyDao;
import com.real.o2o.entity.ProductSellDaily;
import com.real.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 13:45
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    private static Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);

    @Override
    public void dailyCalculate() {
        logger.info("Quartz Running...");
        productSellDailyDao.insertProductSellDaily();
//        统计余下销量为0的商品
        productSellDailyDao.insertDefaultProductSellDaily();
    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition,beginTime,endTime);
    }
}
