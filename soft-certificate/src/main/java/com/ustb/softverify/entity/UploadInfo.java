package com.ustb.softverify.entity;

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

    private Integer tid;
    private String txid;
    private Integer uid;


}
