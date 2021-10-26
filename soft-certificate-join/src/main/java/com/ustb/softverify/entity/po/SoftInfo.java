package com.ustb.softverify.entity.po;

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

    /* 软件存放路径 */
    private String softRemotePath;

    /* 压缩包名称 */
    private String zipName;
    /* 压缩包密码 */
    private String zipPassword;
    /* 上传密码 */
    private String uploadPassword;
    /* 单位名称 */
    private String comName;
    /* 项目名称 */
    private String proName;
    /* 交易地址 */
    private String txid;
    /* 状态 */
    private Integer status;
    /* 核验码 */
    private String verificationCode;

    private String sysId;

    private Integer role;

    /* 标识 */
    private String pid;
}
