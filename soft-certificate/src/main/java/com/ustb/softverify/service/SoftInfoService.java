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

    List<SignFileInfo> SignFileInfos(String pid);

    String findSoftName(String pid);

    void changeStatus(String pid);

    Integer findSoftId(Integer govUserId);

    void insertSignFile(String fileName,String txid,Integer sid);

    List<SignFile> getTxid(Integer sid);

    SoftInfo getSoftInfo(String pid);

    void insertPath(String pid,String path,String zipName);

    SoftInfo getSoftDetail(String pid);

    void insertTxid( String pid, String txid);

    List<SignFileInfo> softFileRecords(String pid);

    Integer getSid(String pid);

    void insertZipPwd(String pid,String zipPassword);

    List<SoftInfo> getAllSoft();
}
