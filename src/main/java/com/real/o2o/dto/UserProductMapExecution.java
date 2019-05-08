package com.real.o2o.dto;

import com.real.o2o.entity.UserProductMap;
import com.real.o2o.enums.UserProductMapStateEnum;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 14:39
 */
public class UserProductMapExecution {

    private int state;

    private String stateInfo;

    private int count;

    private UserProductMap userProductMap;

    private List<UserProductMap> userProductMapList;

    public UserProductMapExecution(){}

    public UserProductMapExecution(UserProductMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum,UserProductMap userProductMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMap = userProductMap;
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum,List<UserProductMap> userProductMapList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMapList = userProductMapList;
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

    public UserProductMap getUserProductMap() {
        return userProductMap;
    }

    public void setUserProductMap(UserProductMap userProductMap) {
        this.userProductMap = userProductMap;
    }

    public List<UserProductMap> getUserProductMapList() {
        return userProductMapList;
    }

    public void setUserProductMapList(List<UserProductMap> userProductMapList) {
        this.userProductMapList = userProductMapList;
    }
}
