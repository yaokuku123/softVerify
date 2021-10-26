package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.po.User;

/**
 * @author WYP
 * @date 2021-10-08 21:11
 */
public interface UserDAO {

    /**
     * 插入用户信息
     * @param user 用户信息
     */
    void insertUser(User user);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void updateUser(User user);

    User getUser(Integer govUserId);
}
