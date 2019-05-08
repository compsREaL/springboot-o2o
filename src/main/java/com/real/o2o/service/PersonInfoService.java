package com.real.o2o.service;

import com.real.o2o.entity.PersonInfo;

/**
 * @author: mabin
 * @create: 2019/5/3 0:56
 */
public interface PersonInfoService {

    /**
     * 通过用户id获取用户信息
     *
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(long userId);
}
