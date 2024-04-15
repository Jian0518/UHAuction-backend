package com.utar.uhauction.common.api;


public interface IErrorCode {
    /**
     * error code: -1 failed;200 success
     *
     * @return error code
     */
    Integer getCode();

    /**
     * error description
     *
     * @return error message
     */
    String getMessage();
}
