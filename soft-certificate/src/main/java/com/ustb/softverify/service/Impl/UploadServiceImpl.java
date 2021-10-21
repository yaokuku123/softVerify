package com.ustb.softverify.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.entity.dto.CertificateInfo;
import com.ustb.softverify.entity.dto.SoftFileInfo;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.BrowserInfoVo;
import com.ustb.softverify.entity.vo.SubmitInfoVo;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.UploadService;
import com.ustb.softverify.webupload.dao.FileRecordMapper;
import com.ustb.softverify.webupload.entity.FileRecord;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Autowired
    private FileRecordMapper fileRecordMapper;

    @Autowired
    private ChainService chainService;

    @Override
    public User getUser(Integer govUserId) {
        return userDAO.getUser(govUserId);
    }

    @Override
    public List<SoftInfo> listSoft(Integer govUserId) {
        return softInfoDAO.listSoft(govUserId);
    }

    @Override
    public void insertUser(User user) {
        userDAO.insertUser(user);
    }

    @Override
    public void insertSoft(SoftInfo softInfo) {
        softInfoDAO.insertSoft(softInfo);
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void updateSoft(SoftInfo softInfo) {
        softInfoDAO.updateSoft(softInfo);
    }

    @Override
    public void updateStatus(Integer govUserId, Integer status) {
        softInfoDAO.updateStatus(govUserId,status);
    }

    @Override
    public SubmitInfoVo getSubmitInfo(Integer govUserId, Integer status) {
        //获取用户信息
        User user = userDAO.getUser(govUserId);
        //获取软件信息
        SoftInfo softInfo = softInfoDAO.getSoftInfoByGovUserId(govUserId, status);
        //获取文档信息
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("gov_user_id",govUserId);
        List<FileRecord> fileRecords = fileRecordMapper.selectList(wrapper);
        //设置数据
        SubmitInfoVo submitInfoVo = new SubmitInfoVo();
        List<SoftFileInfo> softFileInfos = new ArrayList<>();
        for (FileRecord fileRecord : fileRecords) {
            SoftFileInfo softFileInfo = new SoftFileInfo();
            BeanUtils.copyProperties(fileRecord,softFileInfo);
            softFileInfos.add(softFileInfo);
        }
        BeanUtils.copyProperties(user,submitInfoVo);
        BeanUtils.copyProperties(softInfo,submitInfoVo);
        submitInfoVo.setSoftFileList(softFileInfos);
        return submitInfoVo;
    }

    @Override
    public BrowserInfoVo getBrowseInfo(Integer govUserId, Integer status) {
        //获取用户信息
        User user = userDAO.getUser(govUserId);
        //获取软件信息
        SoftInfo softInfo = softInfoDAO.getSoftInfoByGovUserId(govUserId, status);
        //获取文档信息
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("gov_user_id",govUserId);
        List<FileRecord> fileRecords = fileRecordMapper.selectList(wrapper);
        //获取链上信息
        CertificateInfo certificateInfo = getChainInfo(softInfo.getTxid());
        //设置数据
        BrowserInfoVo browserInfoVo = new BrowserInfoVo();
        List<SoftFileInfo> softFileInfos = new ArrayList<>();
        for (FileRecord fileRecord : fileRecords) {
            SoftFileInfo softFileInfo = new SoftFileInfo();
            BeanUtils.copyProperties(fileRecord,softFileInfo);
            softFileInfos.add(softFileInfo);
        }
        BeanUtils.copyProperties(user,browserInfoVo);
        BeanUtils.copyProperties(softInfo,browserInfoVo);
        browserInfoVo.setSoftFileList(softFileInfos);
        browserInfoVo.setCertificateInfo(certificateInfo);
        return browserInfoVo;
    }

    /**
     * 获取链上凭证信息
     * @param txid
     * @return
     */
    private CertificateInfo getChainInfo(String txid) {
        try {
            String fromObj = chainService.getFromObj(txid);
            CertificateInfo certificateInfo = JSONObject.parseObject(JSONObject.parseObject(fromObj).get("certificateInfo").toString(), CertificateInfo.class);
            return certificateInfo;
        } catch (Exception e) {
            throw new CertificateUpChainException();
        }
    }


}
