package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WYP
 * @date 2021-10-24 14:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckPwd {

    private String pid;
    private String password;
}
