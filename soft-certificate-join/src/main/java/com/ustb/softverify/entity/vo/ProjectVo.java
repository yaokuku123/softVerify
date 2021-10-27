package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WYP
 * @date 2021-10-26 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVo {

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
}
