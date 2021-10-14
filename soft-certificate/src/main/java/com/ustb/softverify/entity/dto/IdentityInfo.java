package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
    用户标识和软件名称相关信息的传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer govUserId;
    private String softName;
}
