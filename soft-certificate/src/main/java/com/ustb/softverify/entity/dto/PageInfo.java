package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
    查询数据库信息，封装多张表连查结果，用于向用户展示的数据对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String softName;
    private String softDesc;
    private Integer status;
    private String uname;
    private String company;
    private String phone;
    private Integer govUserId;
}
