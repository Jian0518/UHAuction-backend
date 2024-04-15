package com.utar.uhauction.common.api;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;


@Data
@NoArgsConstructor
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -4153430394359594346L;
    /**
     * status code
     */
    private long code;
    /**
     * result set
     */
    private T data;
    /**
     * interface message
     */
    private String message;

    /**
     * all parameters
     *
     * @param code    status code
     * @param message description
     * @param data    result set
     */
    public ApiResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResult(IErrorCode errorCode) {
        errorCode = Optional.ofNullable(errorCode).orElse(ApiErrorCode.FAILED);
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    /**
     * success
     *
     * @return {code:200,message:operation success,data:customize}
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<T>(ApiErrorCode.SUCCESS.getCode(), ApiErrorCode.SUCCESS.getMessage(), null);
    }

    /**
     * success
     *
     * @param data result set
     * @return {code:200,message:operation success,data:customize}
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(ApiErrorCode.SUCCESS.getCode(), ApiErrorCode.SUCCESS.getMessage(), data);
    }

    /**
     * success
     *
     * @param data    result set
     * @param message customize prompt message
     * @return {code:200,message:customize,data:customize}
     */
    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<T>(ApiErrorCode.SUCCESS.getCode(), message, data);
    }


    public static <T> ApiResult<T> failed() {
        return failed(ApiErrorCode.FAILED);
    }

    /**
     * failed result
     *
     * @param message prompt message
     * @return {code:Enum ApiErrorCode,message:customize,data:null}
     */
    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<T>(ApiErrorCode.FAILED.getCode(), message, null);
    }

    /**
     * failed
     *
     * @param errorCode error code
     * @return {code:encapsulate interface,message:encapsulate interface,data:null}
     */
    public static <T> ApiResult<T> failed(IErrorCode errorCode) {
        return new ApiResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * failed result
     *
     * @param errorCode error code
     * @param message   error message
     * @return {code:Enum ApiErrorCode,message:customize,data:null}
     */
    public static <T> ApiResult<T> failed(IErrorCode errorCode, String message) {
        return new ApiResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * parameter validation failed
     */
    public static <T> ApiResult<T> validateFailed() {
        return failed(ApiErrorCode.VALIDATE_FAILED);
    }

    /**
     * invalid parameter
     *
     * @param message prompt
     */
    public static <T> ApiResult<T> validateFailed(String message) {
        return new ApiResult<T>(ApiErrorCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * not login
     */
    public static <T> ApiResult<T> unauthorized(T data) {
        return new ApiResult<T>(ApiErrorCode.UNAUTHORIZED.getCode(), ApiErrorCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * permission denied
     */
    public static <T> ApiResult<T> forbidden(T data) {
        return new ApiResult<T>(ApiErrorCode.FORBIDDEN.getCode(), ApiErrorCode.FORBIDDEN.getMessage(), data);
    }
}
