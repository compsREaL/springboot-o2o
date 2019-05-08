package com.real.o2o.util;

/**
 * @author: mabin
 * @create: 2019/4/17 17:27
 */
public class PageCalculator {
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
    }
}
