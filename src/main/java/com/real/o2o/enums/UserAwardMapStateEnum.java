package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/7 19:34
 */
public enum  UserAwardMapStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"操作失败"),
    NULL_USERAWARD_ID(-1002,"userAwardId为空"),
    NULL_USERAWARD_INFO(-1003,"传入了空的信息")

    ;
    private int state;

    private String stateInfo;

    UserAwardMapStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static UserAwardMapStateEnum stateOf(int index){
        for (UserAwardMapStateEnum stateEnum:values()){
            if (stateEnum.getState() == index){
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
