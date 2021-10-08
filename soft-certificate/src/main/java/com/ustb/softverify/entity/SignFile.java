package com.ustb.softverify.entity;

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

    private Integer fid;
    private String path;
    private String txid;

    private Integer sid;

}
