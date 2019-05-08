package com.real.o2o.exception;

/**
 * @author: mabin
 * @create: 2019/4/18 16:51
 */
public class ProductCategoryOperationException extends RuntimeException {

    public ProductCategoryOperationException(String errMsg){
        super(errMsg);
    }
}
