package com.real.o2o.dao;

import com.real.o2o.entity.Product;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.entity.ProductImg;
import com.real.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/4/18 18:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest{

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testAInsertProduct(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Product product = new Product();
        product.setProductName("测试商品1");
        product.setProductDesc("测试描述1");
        product.setImgAddr("test1");
        product.setPriority(1);
        product.setEnableStatus(1);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setShop(shop);
        product.setProductCategory(productCategory);
        Product product1 = new Product();
        product1.setProductName("测试商品2");
        product1.setProductDesc("测试描述2");
        product1.setImgAddr("test2");
        product1.setPriority(2);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop);
        product1.setProductCategory(productCategory);
        Product product2 = new Product();
        product2.setProductName("测试商品3");
        product2.setProductDesc("测试描述3");
        product2.setImgAddr("test3");
        product2.setPriority(3);
        product2.setEnableStatus(1);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop);
        product2.setProductCategory(productCategory);
        int effectRows=productDao.insertProduct(product);
        assertEquals(1,effectRows);
        effectRows=productDao.insertProduct(product1);
        assertEquals(1,effectRows);
        effectRows=productDao.insertProduct(product2);
        assertEquals(1,effectRows);
    }

    @Test
    public void testQueryProductById(){
        long productId = 1;
        ProductImg img1 = new ProductImg();
        img1.setImgAddr("111");
        img1.setImgDesc("111");
        img1.setPriority(1);
        img1.setCreateTime(new Date());
        img1.setProductId(productId);
        ProductImg img2 = new ProductImg();
        img2.setImgAddr("222");
        img2.setImgDesc("222");
        img2.setPriority(2);
        img2.setCreateTime(new Date());
        img2.setProductId(productId);
        List<ProductImg> list = new ArrayList<>();
        list.add(img1);
        list.add(img2);
        int effectRows = productImgDao.batchInsertProductImg(list);
        assertEquals(2,effectRows);
        Product product = productDao.queryProductById(productId);
        assertEquals(2,product.getProductImgList().size());
        effectRows = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2,effectRows);
    }

    @Test
    public void testUpdateProduct(){
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1L);
        productCategory.setProductCategoryId(1L);
        product.setProductId(1L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("更新测试");
        int effectRows = productDao.updateProduct(product);
        assertEquals(1,effectRows);
    }

    @Test
    public void testQueryProductList(){
        Product productCondition = new Product();
        //分页查询，返回三条结果
//        List<Product> productList = productDao.queryProductList(productCondition,0,3);
//        assertEquals(3,productList.size());
//        //查询商品总数
//        int count = productDao.queryProductCount(productCondition);
//        assertEquals(5,count);
//        //模糊查询
//        productCondition.setProductName("测试");
//        productList = productDao.queryProductList(productCondition,0,3);
//        assertEquals(3,productList.size());
//        count = productDao.queryProductCount(productCondition);
//        assertEquals(4,count);
//        Shop shop = new Shop();
//        shop.setShopId(1L);
//        productCondition.setShop(shop);
//        List<Product> productList = productDao.queryProductList(productCondition,0,3);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        productCondition.setProductCategory(productCategory);
        List<Product> productList = productDao.queryProductList(productCondition,0,3);
        assertEquals(2,productList.size());
    }

    @Test
    public void testUpdateProductCategoryToNull(){
        int effectRows = productDao.updateProductCategoryToNull(1L);
        assertEquals(2,effectRows);
    }
}