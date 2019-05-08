package com.real.o2o.dao;

import com.real.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author: mabin
 * @create: 2019/5/3 5:09
 */
public interface LocalAuthDao {

    /**
     * 通过账号密码查询对应信息
     *
     * @param username
     * @param password
     * @return
     */
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户ID查询对应的localAuth
     * @param userId
     * @return
     */
    LocalAuth queryLocalByUserId(@Param("userId") long userId);

    /**
     * 添加平台账号
     * @param localAuth
     * @return
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 通过userId，username，password更改用户密码
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @param lastEditTime
     * @return
     */
    int updateLocalAuth(@Param("userId") long userId, @Param("username") String username, @Param("password") String password, @Param("newPassword") String newPassword, @Param("lastEditTime") Date lastEditTime);
}
