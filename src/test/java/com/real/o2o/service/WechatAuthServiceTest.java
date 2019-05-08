package com.real.o2o.service;

import com.real.o2o.dto.WechatAuthExexution;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.WechatAuth;
import com.real.o2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/5/3 0:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest{

    @Autowired
    private WechatAuthService wechatAuthService;

    @Test
    public void testRegister(){
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("jacky");
        personInfo.setGender("男");
        personInfo.setEmail("xxx@xxx.com");
        personInfo.setEnableStatus(1);
        personInfo.setUserType(1);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setCreateTime(new Date());
        wechatAuth.setOpenId("qdasdfqfq");
        WechatAuthExexution wechatAuthExexution = wechatAuthService.register(wechatAuth);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(),wechatAuthExexution.getState());
        WechatAuth wechatAuth1 = wechatAuthService.getWechatAuthByOpenId("qdasdfqfq");
        System.out.println(wechatAuth1.getPersonInfo().getName());
    }

}