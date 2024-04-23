package com.ruby.mall.exception;

import com.ruby.mall.constant.StatusCode;

public class MallException extends Exception{
    public Integer responseCode;

    public MallException(StatusCode statusCode){
        super(statusCode.getResponseBody());
        this.responseCode = statusCode.getResponseCode();
    }
}

