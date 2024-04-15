package com.utar.uhauction.common.api;


public enum ApiErrorCode implements IErrorCode {


    SUCCESS(200, "Operation Success"),

    FAILED(-1, "Operation Failed"),

    UNAUTHORIZED(401, "Not login or token expired"),

    FORBIDDEN(403, "Permission denied"),

    VALIDATE_FAILED(404, "Parameter invalid");

    private final Integer code;
    private final String message;

    ApiErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApiErrorCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
