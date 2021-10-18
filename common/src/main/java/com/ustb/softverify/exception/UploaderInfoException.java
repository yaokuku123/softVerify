package com.ustb.softverify.exception;

/*
    上传软件用户信息异常
 */
public class UploaderInfoException extends RuntimeException {
    public UploaderInfoException() {

    }

    public UploaderInfoException(String message) {
        super(message);
    }
}
