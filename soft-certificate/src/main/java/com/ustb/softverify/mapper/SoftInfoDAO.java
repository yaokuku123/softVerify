package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.SoftInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-09 9:09
 */
public interface SoftInfoDAO {

    void insertSoft(SoftInfo softInfo);

    /**
     * 分页查询软件信息
     * @param page 起始页
     * @param size 每页大小
     * @return 分页查询的软件信息列表
     */
    List<SoftInfo> findByPager(@Param("page") Integer page, @Param("size") Integer size);

    /**
     * 获取未通过审核的软件数量
     * @return 未审核的软件数量
     */
    Long countUnVerifiedSoft();

    /**
     * 根据用户标识和软件名称获取软件路径信息
     * @param govUserId 用户标识
     * @param softName 软件名称
     * @return 软件路径信息
     */
    String getSoftPath(@Param("govUserId") Integer govUserId, @Param("softName") String softName);

    /**
     * 修改软件状态为审核通过 待审核：0，审核通过：1，审核驳回：2
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    void updateSoftStatusToSuccess(@Param("govUserId") Integer govUserId, @Param("softName") String softName);
}
