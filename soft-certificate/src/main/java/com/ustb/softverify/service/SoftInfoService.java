package com.ustb.softverify.service;

import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
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

    List<SignFileInfo> SignFileInfos(Integer pid);

    String findSoftName(Integer pid);

    void changeStatus(Integer govUserId);

    Integer findSoftId(Integer govUserId);

    void insertSignFile(String fileName,String txid,Integer sid);

    List<SignFile> getTxid(Integer sid);

    SoftInfo getSoftInfo(Integer sid);

    void insertPath(String softName,Integer pid,String path,String zipName);

    SoftInfo getSoftDetail(Integer pid);

    void insertTxid( Integer govUserId, String txid);

    List<SignFileInfo> softFileRecords(Integer govUserId);

    Integer getSid(Integer pid);
}
