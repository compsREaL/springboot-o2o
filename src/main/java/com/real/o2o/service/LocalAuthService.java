package com.real.o2o.service;

import com.real.o2o.dto.LocalAuthExecution;
import com.real.o2o.entity.LocalAuth;
import com.real.o2o.exception.LocalAuthOperationException;

/**
 * @author: mabin
 * @create: 2019/5/3 6:05
 */
public interface LocalAuthService {

    /**
     * 通过账号和密码获取平台账号信息
     * @param username
     * @param password
     * @return
     */
    LocalAuth getLocalAuthByUserNameAndPwd(String username, String password);

    /**
     * 通过userId获取平台账号信息
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 绑定微信，生成平台专属的账号
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 修改平台账号的登陆密码
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException;
}
