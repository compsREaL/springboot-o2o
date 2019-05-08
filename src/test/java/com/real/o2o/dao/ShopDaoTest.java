package com.real.o2o.dao;

import com.real.o2o.entity.Area;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/4/12 21:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest{

    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryByShopId(){
        Shop shop = shopDao.queryByShopId(1L);
        System.out.println(shop.getArea().getAreaId()+":"+shop.getArea().getAreaName());
        System.out.println(shop.getShopCategory().getShopCategoryId()+":"+shop.getShopCategory().getShopCategoryName());
    }

    @Test
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setUserId(1L);
        area.setAreaId(1);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(personInfo);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvise("审核中");
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1,effectedNum);
    }

    @Test
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        PersonInfo personInfo = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setUserId(1L);
        area.setAreaId(1);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(personInfo);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺");
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvise("审核中");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1,effectedNum);
    }

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,5,5);
        System.out.println(shopList.size());
        ShopCategory category = new ShopCategory();
        category.setShopCategoryId(1L);
        shopCondition.setShopCategory(category);
        shopList = shopDao.queryShopList(shopCondition,0,7);
        System.out.println(shopList.size());
    }

    @Test
    public void testQueryShopCount(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println(count);
        ShopCategory category = new ShopCategory();
        category.setShopCategoryId(1L);
        shopCondition.setShopCategory(category);
        count = shopDao.queryShopCount(shopCondition);
        System.out.println(count);
    }
}