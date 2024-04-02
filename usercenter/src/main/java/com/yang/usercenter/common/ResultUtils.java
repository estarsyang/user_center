package com.yang.usercenter.common;

public class ResultUtils {
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    public static BaseResponse error(int code, String message, String desc) {
        return new BaseResponse(code,null, message, desc);
    }

    public static BaseResponse error(ErrorCode errorCode, String desc) {
        return new BaseResponse(errorCode.getCode(),null, errorCode.getMessage(), desc);
    }

}
