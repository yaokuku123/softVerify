package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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

    /* 压缩包名称 */
    private String zipName;

    /* 软件存放路径 */
    private String softRemotePath;

    /* 压缩包密码 */
    private String zipPassword;

    /* 状态 */
    private Integer status;

    /* 上传密码 */
    private String uploadPassword;

    /* 交易地址 */
    private String txid;

    /* 核验码 */
    private String verificationCode;

    /* 項目信息 */
    private String project;

    /* 业务号 */
    private String sysId;

    /* 申报单位 */
    private String appliedinst;

    /* 建设单位 */
    private String developinst;

    /* 角色 */
    private Integer role;

    /* 标识 */
    private String pid;

    private Date generateTime;


    private String comName;
    private String proName;

}
