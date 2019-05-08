package com.real.o2o.dao;

import com.real.o2o.entity.ProductImg;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 18:39
 */
public interface ProductImgDao {

    /**
     * 添加商品对应的图片信息，一个商品对应多张图片。
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 删除指定商品下的所有详情图
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

    /**
     * 获取指定商品的所有图片
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);
}
