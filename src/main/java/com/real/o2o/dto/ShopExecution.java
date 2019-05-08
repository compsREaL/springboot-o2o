package com.real.o2o.dto;

import com.real.o2o.entity.Shop;
import com.real.o2o.enums.ShopStateEnum;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/13 10:35
 */
public class ShopExecution {

    //结果状态
    private int status;
    //状态标识
    private String statusInfo;
    //店铺数量
    private int count;
    //操作的shop（增删改）
    private Shop shop;
    //shop列表（查询时）
    private List<Shop> shopList;

    public ShopExecution(){}

    //店铺操作失败时使用的构造器
    public ShopExecution(ShopStateEnum stateEnum){
        this.status = stateEnum.getState();
        this.statusInfo = stateEnum.getStateIndo();
    }
    //店铺操作成功的构造器
    public ShopExecution(ShopStateEnum stateEnum, Shop shop){
        this.status = stateEnum.getState();
        this.statusInfo = stateEnum.getStateIndo();
        this.shop=shop;
    }
    //店铺操作成功的构造器
    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList){
        this.status = stateEnum.getState();
        this.statusInfo = stateEnum.getStateIndo();
        this.shopList=shopList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
