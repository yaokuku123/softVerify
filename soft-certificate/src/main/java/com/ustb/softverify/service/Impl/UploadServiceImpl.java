package com.ustb.softverify.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.entity.dto.CertificateInfo;
import com.ustb.softverify.entity.dto.SoftFileInfo;
import com.ustb.softverify.entity.po.FileUpload;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.*;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.mapper.FileUploadDAO;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.UploadService;
import com.ustb.softverify.utils.EnvUtils;
import com.ustb.softverify.utils.MD5Utils;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Autowired
    private FileUploadDAO fileUploadDAO;

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
    public Integer insertSoft(SoftInfoVo softInfoVo) {
        softInfoVo.setUploadPassword(MD5Utils.code(softInfoVo.getUploadPassword()));
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(softInfoVo,softInfo);
        softInfoDAO.insertSoft(softInfo);
        return softInfo.getSid();
    }

    @Override
    public void updateSoft(SoftInfoVo softInfoVo) {
        softInfoVo.setUploadPassword(MD5Utils.code(softInfoVo.getUploadPassword()));
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(softInfoVo,softInfo);
        softInfoDAO.updateSoft(softInfo);
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
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

        //设置数据

        return null;
    }

    @Override
    public BrowserInfoVo getBrowseInfo(Integer govUserId, Integer status) {
        //获取用户信息
        User user = userDAO.getUser(govUserId);
        //获取软件信息
        SoftInfo softInfo = softInfoDAO.getSoftInfoByGovUserId(govUserId, status);
        //获取文档信息

        //获取链上信息
        CertificateInfo certificateInfo = getChainInfo(softInfo.getTxid());
        //设置数据

        return null;
    }



    @Override
    public void uploadFile(MultipartFile file,String pid,Integer fileType) {
        //文档保存
        String originFileName = file.getOriginalFilename();
        String fileName = originFileName.substring(0, originFileName.lastIndexOf("."));
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        String filePath = uploadFile(fileName,suffix, file);
        //数据信息插入表中
        FileUpload fileUploadDb = fileUploadDAO.getFileUpload(pid,fileType);
        if (fileUploadDb != null) {
            //更新
            fileUploadDb.setFileName(originFileName);
            fileUploadDb.setFilePath(filePath);
            fileUploadDb.setPid(pid);
            fileUploadDb.setFileType(fileType);
            fileUploadDAO.updateFileUpload(fileUploadDb);
        } else {
            //插入
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(originFileName);
            fileUpload.setFilePath(filePath);
            fileUpload.setPid(pid);
            fileUpload.setFileType(fileType);
            fileUploadDAO.insertFileUpload(fileUpload);
        }
    }

    @Override
    public InfoBackVo getInfo(String pid) {
        InfoBackVo res = new InfoBackVo();
        //获取软件信息
        SoftInfo softInfo = softInfoDAO.getSoftInfo(pid);
        if (softInfo != null) {
            BeanUtils.copyProperties(softInfo,res);
        }
        //获取文档列表信息
        List<FileUpload> fileUploadList = fileUploadDAO.listFileUpload(pid);
        List<FileUploadVo> fileUploadVoList = new ArrayList<>();
        if (fileUploadList.size() != 0) {
            for (FileUpload fileUpload : fileUploadList) {
                FileUploadVo fileUploadVo = new FileUploadVo();
                BeanUtils.copyProperties(fileUpload,fileUploadVo);
                fileUploadVoList.add(fileUploadVo);
            }
        }
        res.setFileUploadVoList(fileUploadVoList);
        return res;
    }

    /**
     * 保存上传文档
     *
     * @param fileName 上传文件的名称
     * @param suffix 文件后缀
     * @param file 被测软件
     * @return 上传的文档路径
     */
    private String uploadFile(String fileName,String suffix, MultipartFile file) {
        //拼接文件名，添加uuid
        fileName = fileName + UUID.randomUUID().toString().replaceAll("-","");
        fileName = fileName + suffix;
        //创建文件夹
        File fileDir = new File(EnvUtils.ROOT_PATH );
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        //拼接路径
        String destPath = EnvUtils.ROOT_PATH + fileName;
        File destFile = new File(destPath);
        try {
            //将被测软件保存至本地
            file.transferTo(destFile);
            return destPath;
        } catch (IOException e) {
            throw new FileReadWriteException();
        }
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
