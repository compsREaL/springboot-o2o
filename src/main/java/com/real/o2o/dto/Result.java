package com.real.o2o.dto;

/**
 * @author: mabin
 * @create: 2019/4/18 10:04
 */
public class Result<T> {

    private boolean success;

    private T data;

    private String errMsg;

    private int errCode;

    public Result() {
    }

    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Result(boolean success, String errMsg, int errCode) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
