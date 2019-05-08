package com.real.o2o.dao;

import com.real.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 18:36
 */
public interface ProductDao {

    /**
     * 新增一个商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 通过productId查询商品信息
     * @param productId
     * @return
     */
    Product queryProductById(long productId);

    /**
     * 更新商品信息
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 查询商品列表并分页，可输入的条件有：商品名（模糊查询），商品状态，店铺ID，商品类别
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合查询条件的商品总数
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 删除商品类别前，先将商品类别ID置为空
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);
}
