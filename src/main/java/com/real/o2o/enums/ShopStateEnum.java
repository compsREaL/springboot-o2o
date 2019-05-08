package com.real.o2o.enums;

/**
 * @author: mabin
 * @create: 2019/4/13 10:41
 */
public enum ShopStateEnum {

    CHECK(0, "审核中"),
    OFFLINE(-1, "非法店铺"),
    SUCCESS(1, "操作成功"),
    PASS(2, "审核通过"),
    INNER_ERROR(-1001, "内部系统错误"),
    NULL_SHOP_ID(-1002, "ShopID为空"),
    NULL_SHOP(-1003,"shop信息为空")

    ;

    private int state;
    private String stateIndo;

    private ShopStateEnum(int state, String stateIndo) {
        this.state = state;
        this.stateIndo = stateIndo;
    }

    public static ShopStateEnum stateOf(int state) {
        for (ShopStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }


    public String getStateIndo() {
        return stateIndo;
    }

}
