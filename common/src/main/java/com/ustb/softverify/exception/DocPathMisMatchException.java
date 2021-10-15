package com.ustb.softverify.exception;

/*
    文档路径信息不匹配异常
 */
public class DocPathMisMatchException extends RuntimeException {
    public DocPathMisMatchException() {
    }

    public DocPathMisMatchException(String message) {
        super(message);
    }
}
