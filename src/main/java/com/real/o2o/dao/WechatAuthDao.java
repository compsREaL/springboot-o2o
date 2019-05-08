package com.real.o2o.dao;

import com.real.o2o.entity.WechatAuth;

/**
 * @author: mabin
 * @create: 2019/5/2 11:55
 */
public interface WechatAuthDao {

    /**
     * 通过openId查询对应本平台的微信账号
     *
     * @param openId
     * @return
     */
    WechatAuth queryWechatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信账号
     *
     * @param wechatAuth
     * @return
     */
    int insertWechatAuth(WechatAuth wechatAuth);
}
