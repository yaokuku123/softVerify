package com.ustb.softverify.entity.po;
/*
    审核状态枚举对象
 */
public enum StatusEnum {
    UNVERIFIED(0,"未提交审核"),
    VERIFIED(1,"审核通过"),
    REJECTED(2,"审核驳回");

    private Integer code;
    private String desc;

    private StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
