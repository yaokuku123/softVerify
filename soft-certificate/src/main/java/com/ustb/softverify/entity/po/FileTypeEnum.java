package com.ustb.softverify.entity.po;

/*
    文件类型枚举对象
 */
public enum  FileTypeEnum {

    DIR_FILE(0,"目录文件"),
    IMPORTANT_FILE(1,"重要文件"),
    CONFIG_FILE(2,"配置文件");

    private Integer code;
    private String desc;

    FileTypeEnum(Integer code, String desc) {
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
