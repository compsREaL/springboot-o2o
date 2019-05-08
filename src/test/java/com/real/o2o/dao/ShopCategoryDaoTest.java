package com.real.o2o.dao;

import com.real.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/16 10:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest{

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory(){
//        ShopCategory shopCategoryCondition = new ShopCategory();
//        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
//        assertEquals(2,shopCategoryList.size());
//        ShopCategory parentShopCategory = new ShopCategory();
//        parentShopCategory.setShopCategoryId(1L);
//        shopCategoryCondition.setParent(parentShopCategory);
//        List<ShopCategory> shopCategoryList1 = shopCategoryDao.queryShopCategory(shopCategoryCondition);
//        assertEquals(1,shopCategoryList1.size());
//        System.out.println(shopCategoryList1.get(0).getShopCategoryId()+" :"+shopCategoryList1.get(0).getShopCategoryName());
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
        System.out.println(shopCategoryList.size());
    }

}