package com.ustb.softverify.exception;

/*
    软件压缩包数量不匹配异常
 */
public class CompressNumException extends RuntimeException {

    public CompressNumException() {
    }

    public CompressNumException(String message) {
        super(message);
    }
}
