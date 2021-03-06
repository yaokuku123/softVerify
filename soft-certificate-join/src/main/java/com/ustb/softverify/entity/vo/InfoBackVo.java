package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoBackVo implements Serializable {
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
    /* 标识 */
    private String pid;
    /* 核验码*/
    private String verificationCode;
    /* 状态 */
    private Integer status;
    /* 关联的文档列表 */
    private List<FileUploadVo> fileUploadVoList;
}
