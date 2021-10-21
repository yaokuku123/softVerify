package com.ustb.softverify.service;

import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.SubmitInfoVo;

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

    /**
     * 更新软件状态
     * @param govUserId 用户标识
     * @param status 当前状态
     */
    void updateStatus(Integer govUserId, Integer status);

    /**
     * 获取显示提交页面信息对象
     * @param govUserId 用户标识
     * @param status 状态
     * @return 提交对象信息
     */
    SubmitInfoVo getSubmitInfo(Integer govUserId, Integer status);
}
