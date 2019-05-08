package com.real.o2o.dao;

import com.real.o2o.entity.PersonInfo;

/**
 * @author: mabin
 * @create: 2019/5/2 11:53
 */
public interface PersonInfoDao {

    /**
     * 通过用户id查询用户信息
     *
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * 添加用户信息
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);
}
