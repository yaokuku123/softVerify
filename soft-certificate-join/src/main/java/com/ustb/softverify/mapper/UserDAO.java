package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.po.User;

/**
 * @author WYP
 * @date 2021-10-08 21:11
 */
public interface UserDAO {

    /**
     * 根据用户名获取User对象
     * @param username 用户名
     * @return 用户对象
     */
    User getUser(String username);

}
