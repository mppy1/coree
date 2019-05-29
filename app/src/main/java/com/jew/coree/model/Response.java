package com.jew.coree.model;

/**
 * Created by jinagyong on 2017/5/22.
 * 描述：
 */

public class Response<T> {

    /**
     * data : 3
     * message :
     * returnValue : 1
     * code : 200
     */
    private T data;
    private String message;
    private String returnValue;
    private long code;  //200表示成功，500表示错误

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

}
