package com.real.o2o.dto;

import com.real.o2o.entity.WechatAuth;
import com.real.o2o.enums.WechatAuthStateEnum;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/2 12:59
 */
public class WechatAuthExexution {

    private int state;

    private String stateInfo;

    private int count;

    private WechatAuth wechatAuth;

    private List<WechatAuth> wechatAuthList;

    public WechatAuthExexution() {
    }

    public WechatAuthExexution(WechatAuthStateEnum wechatAuthStateEnum){
        this.state = wechatAuthStateEnum.getState();
        this.stateInfo = wechatAuthStateEnum.getStateInfo();
    }

    public WechatAuthExexution(WechatAuthStateEnum wechatAuthStateEnum, WechatAuth wechatAuth){
        this.state = wechatAuthStateEnum.getState();
        this.stateInfo = wechatAuthStateEnum.getStateInfo();
        this.wechatAuth = wechatAuth;
    }

    public WechatAuthExexution(WechatAuthStateEnum wechatAuthStateEnum, List<WechatAuth> wechatAuthList){
        this.state = wechatAuthStateEnum.getState();
        this.stateInfo = wechatAuthStateEnum.getStateInfo();
        this.wechatAuthList = wechatAuthList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WechatAuth getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(WechatAuth wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public List<WechatAuth> getWechatAuthList() {
        return wechatAuthList;
    }

    public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
        this.wechatAuthList = wechatAuthList;
    }
}
