package com.ustb.softverify.entity.VO;

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

    private Integer uid;
    private String uname;
    private String company;
    private String phone;
    private String softName;
    private String softDesc;
}
