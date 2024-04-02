package com.yang.usercenter.common;


import lombok.Data;

/**
 *
 */
public enum ErrorCode {

    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"params error",""),
    NULL_ERROR(40001,"null params",""),
    NOT_LOGIN(40100,"not login",""),
    NO_AUTH(40100,"no auth",""),
    SYSTEM_ERROR(50000,"system error",""),
    ;

    private final int code;
    private final String message;
    private final String desc;

    ErrorCode(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }
}
