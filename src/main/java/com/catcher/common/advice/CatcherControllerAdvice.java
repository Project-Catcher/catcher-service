package com.catcher.common.advice;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CatcherControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public CommonResponse<String> handle(BaseException e) {
        return CommonResponse.fail(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<String> handle(MethodArgumentNotValidException e) {
        String defaultMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.error(defaultMessage);
        return new CommonResponse<>(400, false, defaultMessage);
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<String> handle(Exception e) {
        log.error("", e);
        return CommonResponse.fail(new BaseException(BaseResponseStatus.RESPONSE_ERROR));
    }
}
