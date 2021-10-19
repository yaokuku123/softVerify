package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.vo.UserSoftInfoVo;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 14:33
 */
public interface SoftInfoDAO {

    List<UserSoftInfoVo> getUploadInfo(Integer govUserId);
}
