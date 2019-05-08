package com.real.o2o.dao;

import com.real.o2o.entity.PersonInfo;
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
public class PersonInfoDaoTest{

    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testQueryPersonInfoById(){
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2);
        assertEquals("lucy",personInfo.getName());
    }

    @Test
    public void testInsertPersonInfo(){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("lucy");
        personInfo.setGender("女");
        personInfo.setEmail("xxx@xxx.com");
        personInfo.setEnableStatus(1);
        personInfo.setUserType(1);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        int efectRows = personInfoDao.insertPersonInfo(personInfo);
        assertEquals(1,efectRows);
    }
}