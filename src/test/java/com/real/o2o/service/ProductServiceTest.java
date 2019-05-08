package com.real.o2o.service;

import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ProductExecution;
import com.real.o2o.entity.Product;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.entity.Shop;
import com.real.o2o.enums.ProductStateEnum;
import com.real.o2o.exception.ProductOperationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/4/18 20:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceTest{

    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() throws FileNotFoundException, ProductOperationException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Product product = new Product();
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("cceshi1");
        product.setProductDesc("ceshi1");
        product.setPriority(10);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        //创建缩略图文件流
        File file = new File("C:/project/xiaohuangren.jpg");
        InputStream inputStream = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(),inputStream);
        File file1 = new File("C:/project/yangleduo.jpg");
        InputStream inputStream1 = new FileInputStream(file1);
        ImageHolder imageHolder1 = new ImageHolder(file1.getName(),inputStream1);
        List<ImageHolder> imageHolderList = new ArrayList<>();
        imageHolderList.add(imageHolder);
        imageHolderList.add(imageHolder1);

        ProductExecution productExecution = productService.addProduction(product,imageHolder,imageHolderList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),productExecution.getState());
    }

    @Test
    public void testModifyProduct() throws FileNotFoundException, ProductOperationException {
        Product product = new Product();
        product.setProductId(1L);
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setProductCategory(productCategory);
        product.setShop(shop);
        product.setProductName("更新商品1");
        product.setProductDesc("更新商品1");
        //创建缩略文件
        File thumbnail = new File("C:/project/yangleduo.jpg");
        InputStream inputStream = new FileInputStream(thumbnail);
        ImageHolder imageHolder = new ImageHolder(thumbnail.getName(),inputStream);
        //创建详情图文件
        File productImg1 = new File("C:/project/xiaohuangren.jpg");
        InputStream productImg1InputStream = new FileInputStream(productImg1);
        ImageHolder holder1 = new ImageHolder(productImg1.getName(),productImg1InputStream);
        File productImg2 = new File("C:/project/yangleduo.jpg");
        InputStream productImg2InputStream = new FileInputStream(productImg2);
        ImageHolder holder2 = new ImageHolder(productImg2.getName(),productImg2InputStream);
        List<ImageHolder> list = new ArrayList<>();
        list.add(holder1);
        list.add(holder2);
        ProductExecution productExecution = productService.modifyProduct(product,imageHolder,list);
        assertEquals(ProductStateEnum.SUCCESS.getState(),productExecution.getState());
    }
}