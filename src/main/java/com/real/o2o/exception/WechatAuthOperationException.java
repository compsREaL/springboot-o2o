package com.real.o2o.exception;

/**
 * @author: mabin
 * @create: 2019/5/2 13:09
 */
public class WechatAuthOperationException extends RuntimeException{
    public WechatAuthOperationException(String errMsg){
        super(errMsg);
    }
}
