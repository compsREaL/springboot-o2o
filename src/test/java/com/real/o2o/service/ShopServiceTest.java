package com.real.o2o.service;

import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ShopExecution;
import com.real.o2o.entity.Area;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.ShopCategory;
import com.real.o2o.enums.ShopStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/4/13 11:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest{

    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() throws FileNotFoundException {
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
        shop.setShopName("test1");
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvise("审核中");
        File file = new File("C:/project/xiaohuangren.jpg");
        InputStream inputStream = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(),inputStream);
        ShopExecution shopExecution = shopService.addShop(shop,imageHolder);
        assertEquals(ShopStateEnum.CHECK.getState(),shopExecution.getStatus());
    }

    @Test
    public void testModifyShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(5L);
        shop.setShopName("更新后店铺名称");
        File file = new File("C:/project/yangleduo.jpg");
        InputStream inputStream = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(),inputStream);
        ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
        System.out.println(shopExecution.getShop().getShopName());
        System.out.println(shopExecution.getShop().getShopImg());
    }

    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        ShopExecution sc = shopService.getShopList(shopCondition,2,5);
        System.out.println(sc.getShopList().size());
        System.out.println(sc.getCount());
    }

}