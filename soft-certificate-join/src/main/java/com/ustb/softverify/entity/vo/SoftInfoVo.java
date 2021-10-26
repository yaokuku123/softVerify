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
    private String comName;
    private String proName;
    private String uploadPassword;
    private String pid;
    private String status;

}
