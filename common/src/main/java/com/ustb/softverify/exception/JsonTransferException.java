package com.ustb.softverify.exception;

/*
    Json数据转换异常
 */
public class JsonTransferException extends RuntimeException {
    public JsonTransferException() {
    }

    public JsonTransferException(String message) {
        super(message);
    }
}
