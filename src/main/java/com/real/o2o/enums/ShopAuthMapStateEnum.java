package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/6 16:49
 */
public enum ShopAuthMapStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"操作失败"),
    NULL_SHOPAUTH_ID(-1002,"shopAuthId为空"),
    NULL_SHOPAUTH_INFO(-1003,"传入了空的信息")
    ;

    private int state;

    private String stateInfo;

    ShopAuthMapStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static ShopAuthMapStateEnum stateOf(int index){
        for (ShopAuthMapStateEnum stateEnum:values()){
            if (stateEnum.getState()==index){
                return stateEnum;
            }
        }
        return null;
    }
}
