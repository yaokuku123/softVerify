package com.ustb.softverify.entity.po;

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

    /* 主键 */
    private Integer uid;

    /* 管理员名称 */
    private String uname;

    /* 管理员电话 */
    private String phone;

    /* 管理员邮箱 */
    private String email;

}
