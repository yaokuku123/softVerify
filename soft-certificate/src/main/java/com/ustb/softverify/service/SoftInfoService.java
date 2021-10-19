package com.ustb.softverify.service;

import com.ustb.softverify.entity.vo.UserSoftInfoVo;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 15:40
 */
public interface SoftInfoService {

    List<UserSoftInfoVo> getUploadInfo(Integer govUserId);
}
