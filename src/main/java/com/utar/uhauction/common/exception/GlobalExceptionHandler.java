package com.utar.uhauction.common.exception;//package com.knox.aurora.common.exception;

import com.utar.uhauction.common.api.ApiResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * catch custom exception
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public ApiResult<Map<String, Object>> handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return ApiResult.failed(e.getErrorCode());
        }
        return ApiResult.failed(e.getMessage());
    }
}
