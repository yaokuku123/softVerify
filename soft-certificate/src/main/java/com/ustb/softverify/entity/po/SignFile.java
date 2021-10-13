package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键 */
    private Integer fid;

    /* 签名存放路径 */
    private String path;

    /* 签名交易ID */
    private String txid;

    /* 外键 */
    private SoftInfo softInfo;

}
