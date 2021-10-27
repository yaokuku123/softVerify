package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-19 14:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;
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
    /* 口令 */
    private String uploadPassword;
    /* 状态 */
    private Integer status;
    /* 标识 */
    private String pid;

}
