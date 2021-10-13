package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.po.User;

/**
 * @author WYP
 * @date 2021-10-08 21:11
 */
public interface UserDAO {

    void insertUser(User user);

    String getUsername(Integer govUserId);
}
