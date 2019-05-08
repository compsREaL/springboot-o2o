package com.real.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户授权token
 * @author: mabin
 * @create: 2019/5/1 19:01
 */
public class UserAccessToken {

    //凭证
    @JsonProperty("access_token")
    private String accessToken;

    //凭证有效时间
    @JsonProperty("expire_in")
    private String expireIn;

    //表示更新令牌，用来获取下一次的访问令牌
    @JsonProperty("refresh_token")
    private String refreshToken;

    //用户在该公众号下的唯一标识
    @JsonProperty("openid")
    private String openId;

    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
