package com.real.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.cache.JedisUtil;
import com.real.o2o.dao.ShopCategoryDao;
import com.real.o2o.entity.ShopCategory;
import com.real.o2o.exception.ShopCategoryOperationException;
import com.real.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/16 10:51
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Override
    @Transactional
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        //定义redis的key前缀
        String key = SHOPCATEGORYLISTKEYS;

        List<ShopCategory> shopCategoryList = null;
        ObjectMapper objectMapper = new ObjectMapper();

        if (shopCategoryCondition == null) {
            //若查询条件为空，则列出所有首页大类
            key = key + "_allfirstlevel";
        } else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            //若parentId非空，则列出该parentId下的所有子类别
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else if (shopCategoryCondition!=null){
            //列出所有子类别，不管其属于哪个类
            key = key + "_allsecondlevel";
        }

        if (!jedisKeys.exists(key)) {
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString = null;
            try {
                jsonString = objectMapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        } else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = objectMapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }
        return shopCategoryList;
    }
}
