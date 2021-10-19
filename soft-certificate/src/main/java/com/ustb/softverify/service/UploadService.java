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

}
