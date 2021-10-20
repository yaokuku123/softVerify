package com.ustb.softverify.exception;

/*
    未上传目录文件异常
 */
public class CoreFileMisException extends RuntimeException {
    public CoreFileMisException() {
    }

    public CoreFileMisException(String message) {
        super(message);
    }
}
