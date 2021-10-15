package com.ustb.softverify.exception;

/*
    软件压缩包格式或大小错误异常
 */
public class CompressFormatException extends RuntimeException {
    public CompressFormatException() {
    }

    public CompressFormatException(String message) {
        super(message);
    }
}
