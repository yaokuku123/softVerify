package com.ustb.softverify.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uid;
    private String uname;
    private String phone;
    private String email;

}
