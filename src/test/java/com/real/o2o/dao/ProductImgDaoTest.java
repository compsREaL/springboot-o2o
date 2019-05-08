package com.real.o2o.dao;

import com.real.o2o.entity.ProductImg;
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
 * @create: 2019/4/18 18:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest {

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testABatchInsertProductImg(){
        ProductImg productImg = new ProductImg();
        productImg.setImgAddr("图片1");
        productImg.setImgDesc("描述图片1");
        productImg.setPriority(1);
        productImg.setCreateTime(new Date());
        productImg.setProductId(1L);
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片2");
        productImg1.setImgDesc("描述图片2");
        productImg1.setPriority(2);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);
        List<ProductImg> productImgList = new ArrayList<>();
        productImgList.add(productImg);
        productImgList.add(productImg1);
        int effectRows = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,effectRows);
    }

    @Test
    public void testBDeleteProductImgByProductId(){
        int effectRows = productImgDao.deleteProductImgByProductId(1L);
        assertEquals(2,effectRows);
    }
}