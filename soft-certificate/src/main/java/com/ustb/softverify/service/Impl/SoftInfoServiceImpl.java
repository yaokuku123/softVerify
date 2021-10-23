package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.service.SoftInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 15:40
 */
@Service
public class SoftInfoServiceImpl implements SoftInfoService {

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Override
    public List<SoftInfoVo> getUploadInfo(Integer govUserId) {
        return softInfoDAO.getUploadInfo(govUserId);
    }

    @Override
    public List<SoftInfoVo> getUnFiledSoftInfo() {
        return softInfoDAO.getUnFiledSoftInfo();
    }

    @Override
    public List<SoftInfoVo> getAllUploadInfo() {
        return softInfoDAO.getAllUploadInfo();
    }

    @Override
    public List<SignFileInfo> SignFileInfos(Integer pid) {
        return softInfoDAO.SignFileInfos(pid);
    }

    @Override
    public String findSoftName(Integer pid) {
        return softInfoDAO.findSoftName(pid);
    }

    @Override
    public void changeStatus(Integer govUserId) {
        softInfoDAO.changeStatus(govUserId);
    }

    @Override
    public Integer findSoftId(Integer govUserId) {
        return softInfoDAO.findSoftId(govUserId);
    }

    @Override
    public void insertSignFile(String fileName, String txid, Integer sid) {
        softInfoDAO.insertSignFile(fileName,txid,sid);
    }

    @Override
    public List<SignFile> getTxid(Integer sid) {
        return softInfoDAO.getTxid(sid);
    }

    @Override
    public SoftInfo getSoftInfo(Integer sid) {
        return softInfoDAO.getSoftInfo(sid);
    }

    @Override
    public void insertPath(String softName, Integer pid,String path,String zipName) {
        softInfoDAO.insertPath(softName,pid,path,zipName);
    }

    @Override
    public SoftInfo getSoftDetail(Integer pid) {
        return softInfoDAO.getSoftDetail(pid);
    }

    @Override
    public void insertTxid(Integer govUserId, String txid) {
        softInfoDAO.insertTxid(govUserId,txid);
    }

    @Override
    public List<SignFileInfo> softFileRecords(Integer govUserId) {
        return softInfoDAO.softFileRecords(govUserId);
    }

    @Override
    public Integer getSid(Integer pid) {
        return softInfoDAO.getSid(pid);
    }
}
