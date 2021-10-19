package com.ustb.softverify.entity.po;
/*
    上传文件状态枚举对象
 */
public enum StatusEnum {
    NOTSAVED(0,"文件未保存"),
    DRAFT(1,"文件存为草稿"),
    SUMMIT(2,"文件提交保存"),
    FILED(3,"文件以归档");

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
