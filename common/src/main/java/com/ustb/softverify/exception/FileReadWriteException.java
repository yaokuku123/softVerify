package com.ustb.softverify.exception;

/*
    软件存储获取异常
 */
public class FileReadWriteException extends RuntimeException {
    public FileReadWriteException() {
    }

    public FileReadWriteException(String message) {
        super(message);
    }
}
