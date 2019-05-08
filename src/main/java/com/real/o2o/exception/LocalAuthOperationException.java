package com.real.o2o.exception;

/**
 * @author: mabin
 * @create: 2019/5/3 6:08
 */
public class LocalAuthOperationException extends RuntimeException {
    public LocalAuthOperationException(String errMsg){
        super(errMsg);
    }
}
