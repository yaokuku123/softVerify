package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserVo;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.UserService;
import com.ustb.softverify.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean login(UserVo userVo) {
        User userDb = userDAO.getUser(userVo.getUsername());
        if (userVo.getRole() == 2 && userDb != null) {
            if (MD5Utils.md5Hex(userVo.getPassword()).equals(userDb.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
