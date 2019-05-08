package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/4/18 10:14
 */
public enum ProductCategoryStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"操作失败"),
    EMPTY_LIST(-1002,"添加数少于1")

    ;

    private int state;

    private String stateInfo;

    private ProductCategoryStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static ProductCategoryStateEnum stateOf(int index){
        for (ProductCategoryStateEnum stateEnum:values()){
            if (stateEnum.getState()==index){
                return stateEnum;
            }
        }
        return null;
    }
}
