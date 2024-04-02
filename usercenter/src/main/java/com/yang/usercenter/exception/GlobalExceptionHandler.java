package com.yang.usercenter.exception;

import com.yang.usercenter.common.BaseResponse;
import com.yang.usercenter.common.ErrorCode;
import com.yang.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException: "+e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDesc());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

}
