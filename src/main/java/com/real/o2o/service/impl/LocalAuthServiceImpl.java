package com.real.o2o.service.impl;

import com.real.o2o.dao.LocalAuthDao;
import com.real.o2o.dto.LocalAuthExecution;
import com.real.o2o.entity.LocalAuth;
import com.real.o2o.enums.LocalAuthStateEnum;
import com.real.o2o.exception.LocalAuthOperationException;
import com.real.o2o.service.LocalAuthService;
import com.real.o2o.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author: mabin
 * @create: 2019/5/3 6:22
 */
@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;

    private static Logger logger = LoggerFactory.getLogger(LocalAuthServiceImpl.class);

    @Override
    public LocalAuth getLocalAuthByUserNameAndPwd(String username, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMD5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        //对传入的localAuth进行空值判断
        if (localAuth==null || localAuth.getUsername()==null || localAuth.getPassword()==null || localAuth.getPersonInfo().getUserId()==null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        //查询此用户是否已经绑定过平台账号
        LocalAuth tempLocalAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if (tempLocalAuth != null){
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            //如果之前没有绑定过平台账号，则新建一个平台账号并进行绑定操作
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5.getMD5(localAuth.getPassword()));
            int effectRows = localAuthDao.insertLocalAuth(localAuth);
            if (effectRows<=0){
                throw new LocalAuthOperationException("账号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
            }
        } catch (Exception e){
            logger.error("insertLocalAuth error:{}",e.getMessage());
            throw new LocalAuthOperationException("insertLocalAuth error:"+e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        //非空判断
        if (userId!=null && username!=null && password!=null && newPassword!=null && !password.equals(newPassword)){
            try {
                //更新密码并使用MD5加密
                int effectRows = localAuthDao.updateLocalAuth(userId,username, MD5.getMD5(password), MD5.getMD5(newPassword),new Date());

                if (effectRows<=0){
                    throw new LocalAuthOperationException("密码更新失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e){
                logger.error("modifyLocalAuth error:{}",e.getMessage());
                throw new LocalAuthOperationException("modifyLocalAuth error:" + e.getMessage());
            }
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
