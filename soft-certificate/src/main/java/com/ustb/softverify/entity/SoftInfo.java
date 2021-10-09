package com.ustb.softverify.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SoftInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键 */
    private Integer sid;

    /* 软件名称 */
    private String name;

    /* 软件描述信息 */
    private String desc;

    /* 软件存放路径 */
    private String softPath;

    /* 说明文档存放路径 */
    private String docPath;

    /* 软件包hash */
    private String hash;

    /* 审核状态 */
    private Integer status;

    /* 外键 */
    private User user;



}
