package com.real.o2o.dao;

import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.WechatAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/5/2 12:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTest{

    @Autowired
    private WechatAuthDao wechatAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testQueryWechatAuthByOpenId(){
        WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("1111111111");
        assertEquals("lucy",wechatAuth.getPersonInfo().getName());
    }

    @Test
    public void insertWechatAuth(){
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2L);
        WechatAuth wechatAuth = new WechatAuth();
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId("1111111111");
        wechatAuth.setCreateTime(new Date());
        int effectRows = wechatAuthDao.insertWechatAuth(wechatAuth);
        assertEquals(1,effectRows);
    }


}