package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WYP
 * @date 2021-10-08 21:18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;


    @Override
    public void insertUser(User user) {
        userDAO.insertUser(user);
    }
}
