package com.ustb.softverify.service;

import com.ustb.softverify.entity.vo.PageRequest;
import com.ustb.softverify.entity.vo.PageResult;

public interface SoftVerifyService {

    /**
     * 分页查询接口
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest);

    /**
     * 根据用户标识和软件名称获取软件路径
     * @param govUserId 用户标识
     * @param softName 软件名称
     * @return 软件路径信息
     */
    String getSoftPath(Integer govUserId,String softName);

    /**
     * 分页查询已审核通过的软件信息
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPageSuccess(PageRequest pageRequest);

    /**
     * 分页查询审核驳回的软件信息
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPageFail(PageRequest pageRequest);


    /**
     * 审核通过
     * @param govUserId 用户标识
     * @param softName 软件名称
     */
    void verifySuccess(Integer govUserId, String softName);

    /**
     * 审核失败
     * @param govUserId 用户标识
     * @param softName 软件名称
     */
    void verifyFail(Integer govUserId, String softName);
}
