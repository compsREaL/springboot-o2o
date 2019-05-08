package com.real.o2o.service.impl;

import com.real.o2o.dao.PersonInfoDao;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: mabin
 * @create: 2019/5/3 0:58
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}
