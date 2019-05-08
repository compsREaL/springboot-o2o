package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/2 13:02
 */
public enum WechatAuthStateEnum {

    LOGIN_FAIL(-1,"openId有误"),
    SUCCESS(0,"操作成功"),
    NULL_AUTH_INFO(-1001,"注册信息为空");

    private int state;

    private String stateInfo;

    private WechatAuthStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
