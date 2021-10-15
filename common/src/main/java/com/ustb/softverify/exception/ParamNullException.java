package com.ustb.softverify.exception;

public class ParamNullException extends RuntimeException {
    public ParamNullException() {
    }

    public ParamNullException(String message) {
        super(message);
    }
}
