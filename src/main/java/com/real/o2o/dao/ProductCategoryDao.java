package com.real.o2o.dao;

import com.real.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 9:40
 */
public interface ProductCategoryDao {


    /**
     * 根据商品类别id和店铺id删除商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
    /**
     * 批量新增商品类别
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);
    /**
     * 通过shopId查询该店铺下的所有商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);
}
