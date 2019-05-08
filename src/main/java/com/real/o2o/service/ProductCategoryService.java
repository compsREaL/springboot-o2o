package com.real.o2o.service;

import com.real.o2o.dto.ProductCategoryExecution;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.exception.ProductCategoryOperationException;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 9:55
 */
public interface ProductCategoryService {

    /**
     * 先将该商品类别下的所有商品的类别id置为空，再删除该商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;

    /**
     * 批量添加操作
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;
    /**
     * 通过shopId查询该店铺下的所有商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

}
