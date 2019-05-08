package com.real.o2o.dto;

import com.real.o2o.entity.ShopAuthMap;
import com.real.o2o.enums.ShopAuthMapStateEnum;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/6 16:48
 */
public class ShopAuthMapExecution {

    private int state;

    private String stateInfo;

    private Integer count;

    private ShopAuthMap shopAuthMap;

    private List<ShopAuthMap> shopAuthMapList;

    public ShopAuthMapExecution(){}

    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, ShopAuthMap shopAuthMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMap = shopAuthMap;
    }

    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, List<ShopAuthMap> shopAuthMapList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMapList = shopAuthMapList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ShopAuthMap getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMap shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMap> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }
}
