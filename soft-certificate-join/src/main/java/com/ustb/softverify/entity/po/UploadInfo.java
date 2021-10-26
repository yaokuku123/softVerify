package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键 */
    private Integer tid;

    /* 交易ID */
    private String txid;

    /* 关联外键 */
    private User user;


}
