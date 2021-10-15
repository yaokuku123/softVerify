package com.ustb.softverify.domain;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ResponseCodeEnum
 * Author: yaoqijun
 * Date: 2021/5/25 14:08
 */
public enum ResponseCodeEnum implements BaseErrorInfoInterface {
    SUCCESS(20000, "成功"),
    FAIL(20001, "失败");

    private final int code;
    private final String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
