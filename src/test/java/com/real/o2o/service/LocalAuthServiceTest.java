package com.real.o2o.service;

import com.real.o2o.dto.LocalAuthExecution;
import com.real.o2o.entity.LocalAuth;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.enums.LocalAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/5/3 7:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest {

    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testBindLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(3L);
        localAuth.setPersonInfo(personInfo);
        localAuth.setUsername("aaa");
        localAuth.setPassword("bbb");
        LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
        assertEquals(LocalAuthStateEnum.ONLY_ONE_ACCOUNT.getState(),localAuthExecution.getState());
        personInfo.setUserId(2L);
        localAuthExecution = localAuthService.bindLocalAuth(localAuth);
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(),localAuthExecution.getState());
    }

    @Test
    public void testModifyLocalAuth(){

        LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(2L,"aaa","bbb","ccc");
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(),localAuthExecution.getState());
    }
}