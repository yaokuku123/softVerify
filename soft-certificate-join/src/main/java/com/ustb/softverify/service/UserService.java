package com.ustb.softverify.service;

import com.ustb.softverify.entity.vo.UserVo;

public interface UserService {

    /**
     * 管理员用户登陆正确性判断
     * @param userVo
     * @return
     */
    boolean login(UserVo userVo);
}
