package com.real.o2o.dto;

import java.util.HashSet;

/**
 * echart图表中的xAxis项
 * @author: mabin
 * @create: 2019/5/7 16:48
 */
public class EchartXAxis {
    private String type= "category";
    //去重
    private HashSet<String> data;

    public HashSet<String> getData() {
        return data;
    }

    public void setData(HashSet<String> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }
}
