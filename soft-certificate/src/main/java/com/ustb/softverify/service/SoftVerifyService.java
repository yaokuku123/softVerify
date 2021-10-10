package com.ustb.softverify.service;

import com.ustb.softverify.entity.VO.PageRequest;
import com.ustb.softverify.entity.VO.PageResult;

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
     * 修改软件状态为审核通过 待审核：0，审核通过：1，审核驳回：2
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    void updateSoftStatusToSuccess(Integer govUserId,String softName);

    /**
     * 修改软件状态为审核驳回 待审核：0，审核通过：1，审核驳回：2
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    void updateSoftStatusToFail(Integer govUserId,String softName);

    /**
     * 删除指定路径下的文件
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    void deleteSoftAndDoc(Integer govUserId, String softName);

}
