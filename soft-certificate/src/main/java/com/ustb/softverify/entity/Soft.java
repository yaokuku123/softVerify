package com.ustb.softverify.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-08 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Soft implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer sid;
    private String name;
    private String desc;
    private String softPath;
    private String docPath;
    private String hash;
    private Integer status;

    private Integer uid;



}
