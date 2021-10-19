package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
    private String softName;

    /* 软件描述信息 */
    private String softDesc;

    /* 软件存放路径 */
    private String softRemotePath;

    /* 文件状态 */
    private Integer status;

    /* 用户标识 */
    private String govUserId;

    /* 签名文件 */
    private List<SignFile> signFiles;



}
