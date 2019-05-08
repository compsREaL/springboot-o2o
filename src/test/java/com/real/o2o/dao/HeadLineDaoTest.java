package com.real.o2o.dao;

import com.real.o2o.entity.HeadLine;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: mabin
 * @create: 2019/4/29 15:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder
public class HeadLineDaoTest{

    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQueryHeadLine(){

        List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
        assertEquals(1,headLineList.size());
    }
}