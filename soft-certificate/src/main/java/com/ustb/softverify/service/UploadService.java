package com.ustb.softverify.service;

import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;

import java.util.List;

public interface UploadService {

    /**
     * 根据用户标识获取用户对象信息
     * @param govUserId 用户标识
     * @return 对象信息，没有获取到则为null
     */
    User getUser(Integer govUserId);

    /**
     * 根据用户标识获取软件列表
     * @param govUserId 用户标识
     * @return 软件列表信息
     */
    List<SoftInfo> listSoft(Integer govUserId);

    /**
     * 插入用户信息
     * @param user 用户信息
     */
    void insertUser(User user);

    /**
     * 插入软件信息
     * @param softInfo
     */
    void insertSoft(SoftInfo softInfo);

    void updateUser(User user);

    void updateSoft(SoftInfo softInfo);
}
