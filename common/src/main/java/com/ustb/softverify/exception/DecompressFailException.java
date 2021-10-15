package com.ustb.softverify.exception;

/*
    压缩包解压失败异常
 */
public class DecompressFailException extends RuntimeException {
    public DecompressFailException() {
    }

    public DecompressFailException(String message) {
        super(message);
    }
}
