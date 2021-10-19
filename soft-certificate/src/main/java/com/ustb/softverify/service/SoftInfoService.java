package com.ustb.softverify.service;

import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 15:40
 */
public interface SoftInfoService {

    List<SoftInfoVo> getUploadInfo(Integer govUserId);

    List<SoftInfoVo> getUnFiledSoftInfo();

    List<SoftInfoVo> getAllUploadInfo();

    List<SignFileInfo> SignFileInfos(Integer govUserId);

    String findSoftName(Integer govUserId);
}
