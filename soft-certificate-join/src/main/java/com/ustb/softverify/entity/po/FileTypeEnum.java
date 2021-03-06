package com.ustb.softverify.entity.po;

/*
    文件类型枚举对象
 */
public enum  FileTypeEnum {

    DIR_FILE(0,"目录文件"),
    IMPORTANT_FILE1(1,"重要文件1"),
    IMPORTANT_FILE2(2,"重要文件2"),
    IMPORTANT_FILE3(3,"重要文件3"),
    CONFIG_FILE(4,"配置文件");

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
