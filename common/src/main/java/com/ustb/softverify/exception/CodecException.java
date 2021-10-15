package com.ustb.softverify.exception;

/*
    编解码数据异常
 */
public class CodecException extends RuntimeException {
    public CodecException() {
    }

    public CodecException(String message) {
        super(message);
    }
}
