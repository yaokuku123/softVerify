package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-26 20:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mobile implements Serializable {

    private String mobile;
    private String code;
}
