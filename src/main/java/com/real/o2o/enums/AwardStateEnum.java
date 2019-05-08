package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/5/7 21:14
 */
public enum AwardStateEnum {

    FFLINE(-1, "非法奖品"),
    SUCCESS(0, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "奖品信息为空")


    ;

    private int state;

    private String stateInfo;

    AwardStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static AwardStateEnum stateOf(int index){
        for (AwardStateEnum stateEnum:values()){
            if (stateEnum.getState() == index){
                return stateEnum;
            }
        }
        return null;
    }
}
