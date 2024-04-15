package com.utar.uhauction.common.exception;

import com.utar.uhauction.common.api.IErrorCode;


public class ApiAsserts {
    /**
     * throw exception
     *
     * @param message error description
     */
    public static void fail(String message) {
        throw new ApiException(message);
    }

    /**
     * throw exception
     *
     * @param errorCode error code
     */
    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
