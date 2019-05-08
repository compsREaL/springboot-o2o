package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/7 18:50
 */
public enum UserShopMapStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"操作失败"),
    NULL_USERSHOP_ID(-1002,"userShopId为空"),
    NULL_USERSHOP_INFO(-1003,"传入了空的信息")
            ;

    private int state;

    private String stateInfo;

    UserShopMapStateEnum(int state,String stateInfo){
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
