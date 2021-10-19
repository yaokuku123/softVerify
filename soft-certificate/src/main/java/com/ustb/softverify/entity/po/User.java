package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author WYP
 * @date 2021-10-08 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 用户标识 */
    private Integer uid;

    /* 用户名称 */
    private String uname;

    /* 用户所属公司信息 */
    private String company;

    /* 用户联系方式 */
    private String phone;

    /* 评审平台用户标识 */
    private Integer govUserId;

}
