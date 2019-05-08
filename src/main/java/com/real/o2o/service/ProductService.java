package com.real.o2o.service;

import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ProductExecution;
import com.real.o2o.entity.Product;
import com.real.o2o.exception.ProductOperationException;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 19:21
 */
public interface ProductService {

    /**
     * 添加商品
     * 1.处理缩略图
     * 2.处理商品详情图片
     * 3.添加商品信息
     * @param product
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduction(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException;

    /**
     * 通过商品id查询商品
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 修改商品信息以及图片处理
     * @param product
     */
    ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException;

    /**
     * 查询商品列表并分页，可输入的条件有：商品名（模糊查询），商品状态，店铺ID，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

}
