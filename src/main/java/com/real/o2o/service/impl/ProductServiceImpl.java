package com.real.o2o.service.impl;

import com.real.o2o.dao.ProductDao;
import com.real.o2o.dao.ProductImgDao;
import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ProductExecution;
import com.real.o2o.entity.Product;
import com.real.o2o.entity.ProductImg;
import com.real.o2o.enums.ProductStateEnum;
import com.real.o2o.exception.ProductOperationException;
import com.real.o2o.service.ProductService;
import com.real.o2o.util.ImageUtil;
import com.real.o2o.util.PageCalculator;
import com.real.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/18 19:53
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 1.处理缩略图，获取缩略图相对路径并赋值给product
     * 2.往tb_product写入商品信息，获取productId
     * 3.结合productId批量处理商品详情图
     * 4.将商品详情图列表批量插入tb_product_img中
     *
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution addProduction(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认商品为上架状态
            product.setEnableStatus(1);
            //判断并添加商品缩略图
            if (imageHolder != null) {
                addImageHolder(product, imageHolder);
            }
            try {
                int effectRows = productDao.insertProduct(product);
                if (effectRows <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败" + e.toString());
            }
            if (imageHolderList != null && imageHolderList.size() > 0) {
                addProductImgList(product, imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY_LIST);
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    /**
     * 若缩略图参数有值，则处理缩略图
     * 若原先存在缩略图，则先删除再添加
     * 若商品详情图列表参数有值，则进行相同操作
     * 将tb_product_img下面该商品原先的商品详情图记录删除
     * 更新tb_product信息
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException {
        if (product!=null && product.getShop()!=null && product.getShop().getShopId()!=null){
            product.setLastEditTime(new Date());
            //若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加新图
            if (imageHolder!=null){
                //先获取原有信息，因为原有信息中有原图片地址
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr()!=null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addImageHolder(product,imageHolder);
            }
            //若商品详情图不为空，则删除原先的图片，并添加新的图片
            if (imageHolderList!=null && imageHolderList.size()>0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,imageHolderList);
            }
            try {
                //更新商品信息
                int effectRows = productDao.updateProduct(product);
                if (effectRows<=0){
                    throw new ProductOperationException("商品信息更新失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException("商品信息更新失败:"+e.toString());
            }
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY_LIST);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //页码转化为对应的数据库的行码，并调用dao取回页码的商品列表
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(productCondition,rowIndex,pageSize);
        //基于同样的查询条件返回该查询条件下的商品总数
        int count = productDao.queryProductCount(productCondition);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setCount(count);
        productExecution.setProductList(productList);
        return productExecution;
    }

    private void deleteProductImgList(Long productId) {
        //获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //遍历并进行删除
        for (ProductImg productImg : productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除数据库中原有的图片信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    /**
     * 批量添加商品详情图
     * @param product
     * @param imageHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> imageHolderList) throws ProductOperationException {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        //遍历图片流，并添加进ProductImg实体类中
        for (ImageHolder imageHolder:imageHolderList){
            String imgAddr = ImageUtil.generateNormalImg(imageHolder,dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }

        if (productImgList.size()>0){
            try {
                int effectRows = productImgDao.batchInsertProductImg(productImgList);
                if (effectRows<=0){
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图片失败"+e.toString());
            }
        }
    }

    private void addImageHolder(Product product, ImageHolder imageHolder) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(imageHolder,dest);
        product.setImgAddr(thumbnailAddr);
    }
}
