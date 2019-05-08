package com.real.o2o.service;

import com.real.o2o.dto.WechatAuthExexution;
import com.real.o2o.entity.WechatAuth;
import com.real.o2o.exception.WechatAuthOperationException;

/**
 * @author: mabin
 * @create: 2019/5/2 12:57
 */
public interface WechatAuthService {

    /**
     * 通过openId查询对应的微信账号
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     * @param wechatAuth
     * @return
     */
    WechatAuthExexution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
