package com.ustb.softverify.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键 */
    private Integer fid;

    /* 文件名 */
    private String fileName;

    /* 签名交易ID */
    private String txid;

    /* 外键 */
    private SoftInfo softInfo;

}
