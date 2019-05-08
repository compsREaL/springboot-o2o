package com.real.o2o.dto;

import com.real.o2o.entity.UserShopMap;
import com.real.o2o.enums.UserShopMapStateEnum;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 18:48
 */
public class UserShopMapExecution {

    private int state;

    private String stateInfo;

    private int count;

    private UserShopMap userShopMap;

    private List<UserShopMap> userShopMapList;

    public UserShopMapExecution(){}

    public UserShopMapExecution(UserShopMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum,UserShopMap userShopMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum,List<UserShopMap> userShopMapList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMapList = userShopMapList;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }
}
