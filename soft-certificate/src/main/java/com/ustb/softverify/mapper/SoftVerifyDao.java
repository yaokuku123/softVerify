package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.SoftInfo;
import com.ustb.softverify.entity.VO.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SoftVerifyDao {
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

}
