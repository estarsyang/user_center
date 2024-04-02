package com.yang.usercenter.common;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private int code;
    private T data;
    private String message;
    private String desc;

    public BaseResponse(int code, T data, String message,String desc) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.desc = desc;
    }

    public BaseResponse(int code, T data,String message) {
        this(code,data,message,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDesc());
    }
}
