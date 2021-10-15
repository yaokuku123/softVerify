package com.ustb.softverify.exception;

/*
    软件压缩包超过指定大小异常
 */
public class CompressSizeException extends RuntimeException {

    public CompressSizeException() {
    }

    public CompressSizeException(String message) {
        super(message);
    }
}
