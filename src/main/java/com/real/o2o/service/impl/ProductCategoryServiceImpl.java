package com.real.o2o.service.impl;

import com.real.o2o.dao.ProductCategoryDao;
import com.real.o2o.dao.ProductDao;
import com.real.o2o.dto.ProductCategoryExecution;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.enums.ProductCategoryStateEnum;
import com.real.o2o.exception.ProductCategoryOperationException;
import com.real.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 9:56
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        //解除tb_product里的商品与该productCategory的关联
        try {
            int effectRows = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectRows<0){
                throw new ProductCategoryOperationException("商品类别更新失败");
            }
        } catch (Exception e){
            throw new ProductCategoryOperationException("DeleteProductCategory Error:"+e.getMessage());
        }
        //删除该productCategory
        try {
            int effectRows = productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if (effectRows<=0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList!=null && productCategoryList.size()>0){
            try {
                int effectRows = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectRows<=0){
                    throw new ProductCategoryOperationException("商品类别创建失败");
                }else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (ProductCategoryOperationException e) {
                throw new ProductCategoryOperationException("batchAddProductCategory error:"+e.getMessage());
            }
        } else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }
}
