package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/7 14:40
 */
public enum UserProductMapStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"操作失败"),
    NULL_USERPRODUCT_ID(-1002,"userProductId为空"),
    NULL_USERPRODUCT_INFO(-1003,"传入了空的信息")
    ;

    private int state;

    private String stateInfo;

    UserProductMapStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static UserProductMapStateEnum stateOf(int index){
        for (UserProductMapStateEnum stateEnum:values()){
            if (stateEnum.getState()==index){
                return stateEnum;
            }
        }
        return null;
    }
}
