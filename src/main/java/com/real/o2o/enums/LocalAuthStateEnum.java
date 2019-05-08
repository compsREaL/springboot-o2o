package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/3 6:10
 */
public enum LocalAuthStateEnum {

    LOGIN_FAIL(-1,"用户登录失败"),
    SUCCESS(0,"操作成功"),
    NULL_AUTH_INFO(-1006,"注册信息为空"),
    ONLY_ONE_ACCOUNT(-1007,"最多只能绑定一个账号")

    ;

    private int state;

    private String stateInfo;

    private LocalAuthStateEnum(int state,String stateInfo){
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
