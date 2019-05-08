package com.real.o2o.dto;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 16:50
 */
public class EchartSeries {
    private String name;
    private String type="bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }
}
