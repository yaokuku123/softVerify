package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUploadInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer uid;
    private Integer govUserId;
    private String uname;
    private String company;
    private String phone;
    private Integer sid;
    private String softName;
    private String softDesc;
}
