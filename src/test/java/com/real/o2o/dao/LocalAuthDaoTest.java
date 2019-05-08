package com.real.o2o.dao;

import com.real.o2o.entity.LocalAuth;
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
 * @create: 2019/5/3 5:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {

    @Autowired
    private LocalAuthDao localAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testQueryLocalByUserNameAndPwd(){
        String username = "aaaa";
        String password = "bbbb";
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username,password);
        assertEquals(1L,localAuth.getLocalAuthId().longValue());
    }

    @Test
    public void testQueryLocalByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(3);
        assertEquals("jacky",localAuth.getPersonInfo().getName());
    }

    @Test
    public void testInsertLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(3L);
        localAuth.setPersonInfo(personInfo);
        localAuth.setUsername("aaaa");
        localAuth.setPassword("bbbb");
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        int effectRows = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1,effectRows);
    }

    @Test
    public void testUpdateLocalAuth(){
        long userId = 3L;
        String username = "aaaa";
        String password = "bbbb";
        String newPassword = "new_password";
        int effectRows = localAuthDao.updateLocalAuth(userId,username,password,newPassword,new Date());
        assertEquals(1,effectRows);
    }
}