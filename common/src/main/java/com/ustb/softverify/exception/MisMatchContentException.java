package com.ustb.softverify.exception;

/*
    重要文档未匹配目录文件异常
 */
public class MisMatchContentException extends RuntimeException {
    public MisMatchContentException() {
    }

    public MisMatchContentException(String message) {
        super(message);
    }
}
