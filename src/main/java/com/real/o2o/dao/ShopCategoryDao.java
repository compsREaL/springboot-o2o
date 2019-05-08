package com.real.o2o.dao;

import com.real.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/16 10:19
 */
public interface ShopCategoryDao {
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
