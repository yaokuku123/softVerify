package com.ustb.softverify.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.CertificateInfo;
import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.entity.po.FileTypeEnum;
import com.ustb.softverify.entity.po.FileUpload;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.*;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.mapper.FileUploadDAO;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UploadInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.UploadService;
import com.ustb.softverify.utils.EnvUtils;
import com.ustb.softverify.utils.FileUtil;
import com.ustb.softverify.utils.MD5Utils;
import com.ustb.softverify.utils.ReadTxt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    public List<SoftInfo> listSoft(Integer govUserId) {
        return softInfoDAO.listSoft(govUserId);
    }


    @Override
    public void updateSoft(SoftInfoVo softInfoVo) {
        softInfoVo.setUploadPassword(MD5Utils.code(softInfoVo.getUploadPassword()));
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(softInfoVo,softInfo);
        softInfoDAO.updateSoft(softInfo);
    }


    @Override
    public void updateStatus(Integer govUserId, Integer status) {
        softInfoDAO.updateStatus(govUserId,status);
    }




    @Override
    public void uploadFile(MultipartFile file,String pid,Integer fileType) {
        //????????????
        String originFileName = file.getOriginalFilename();
        File tmpFile = uploadFile(originFileName,pid,file);
        //????????????????????????
        FileUpload fileUploadDb = fileUploadDAO.getFileUpload(pid,fileType);
        if (fileUploadDb != null) {
            //???????????????????????????
            FileUtil.delete(fileUploadDb.getFilePath());
            //??????
            fileUploadDb.setFileSize(tmpFile.length());
            fileUploadDb.setFileName(originFileName);
            fileUploadDb.setFilePath(tmpFile.getAbsolutePath());
            fileUploadDb.setPid(pid);
            fileUploadDb.setFileType(fileType);
            fileUploadDAO.updateFileUpload(fileUploadDb);
        } else {
            //??????
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileSize(tmpFile.length());
            fileUpload.setFileName(originFileName);
            fileUpload.setFilePath(tmpFile.getAbsolutePath());
            fileUpload.setPid(pid);
            fileUpload.setFileType(fileType);
            fileUploadDAO.insertFileUpload(fileUpload);
        }
    }

    @Override
    public void deleteFile(String pid, Integer fileType) {
        //??????????????????
        FileUpload fileUpload = fileUploadDAO.getFileUpload(pid, fileType);
        FileUtil.delete(fileUpload.getFilePath());
        //???????????????
        fileUploadDAO.deleteFileUpload(pid,fileType);
    }

    /**
     * ??????????????????
     *
     * @param fileName ?????????????????????
     *
     * @param file ????????????
     * @return ?????????????????????
     */
    private File uploadFile(String fileName,String pid, MultipartFile file) {
        //????????????
        String destPath = EnvUtils.ROOT_PATH + Calendar.getInstance().get(Calendar.YEAR) + File.separator + pid
                + File.separator + "origin" + File.separator + fileName;
        //???????????????
        File destFile = new File(destPath);
        if (!destFile.getParentFile().exists()) { // ??????????????????????????????????????????
            destFile.getParentFile().mkdirs();
        }
        try {
            //??????????????????????????????
            file.transferTo(destFile);
            return destFile;
        } catch (IOException e) {
            throw new FileReadWriteException();
        }
    }

    /**
     * ????????????????????????
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

    @Override
    public boolean submitInfo(SoftInfoVo softInfoVo) {
        //?????????????????????
        SoftInfo softInfoDb = softInfoDAO.getSoftInfo(softInfoVo.getPid());
        //???????????????????????????
        softInfoDb.setUploadPassword(MD5Utils.code(softInfoVo.getUploadPassword()));
        softInfoDAO.updateSoft(softInfoDb);

        //??????????????????
        String filePath = null;
        List<FileUpload> fileUploadList = fileUploadDAO.listFileUpload(softInfoVo.getPid());
        List<CompInfo> compInfos = new ArrayList<>();
        for (FileUpload fileUpload : fileUploadList) {
            //??????????????????????????????????????????
            if (fileUpload.getFileType().equals(FileTypeEnum.DIR_FILE.getCode())) {
                filePath = fileUpload.getFilePath();
            }
            //????????????????????????????????????????????????????????????????????????
            if (!fileUpload.getFileType().equals(FileTypeEnum.DIR_FILE.getCode()) &&
                    !fileUpload.getFileType().equals(FileTypeEnum.CONFIG_FILE.getCode())  ) {
                CompInfo compInfo = new CompInfo();
                compInfo.setOrgName(fileUpload.getFileName());
                compInfo.setFileSize(fileUpload.getFileSize());
                compInfos.add(compInfo);
            }
        }
        if (filePath == null){
            return false;
        }
        boolean flag = ReadTxt.comp2txt(filePath, compInfos);
        return flag;
    }

    @Override
    public String getUploadFilePath(String pid, Integer fileType) {
        return fileUploadDAO.getFileUpload(pid,fileType).getFilePath();
    }

    @Override
    public SoftInfoVo getResponseInfo(ProjectVo projectVo) {
        //??????sysid???????????????????????????
        SoftInfo softInfoDB = fileUploadDAO.findBySysId(projectVo.getSysId());
        if (softInfoDB == null){
            //???????????????
            SoftInfoVo softInfoVo = new SoftInfoVo();
            BeanUtils.copyProperties(projectVo,softInfoVo);
            softInfoVo.setStatus(1);
            String pid = UUID.randomUUID().toString();
            softInfoVo.setPid(pid);
            fileUploadDAO.insertSoftInfo(softInfoVo);
            return softInfoVo;
        } else {
            //??????????????????
            SoftInfoVo softInfoVo = new SoftInfoVo();
            BeanUtils.copyProperties(softInfoDB,softInfoVo);
            return softInfoVo;
        }
    }


    @Override
    public InfoBackVo getInfo(String pid) {
        InfoBackVo res = new InfoBackVo();
        SoftInfo softInfo = fileUploadDAO.getProjectInfo(pid);
        BeanUtils.copyProperties(softInfo,res);
        //????????????????????????
        List<FileUpload> fileUploadList = fileUploadDAO.listFileUpload(softInfo.getPid());
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

    @Override
    public SoftInfo getProjectInfo(String pid) {
        return fileUploadDAO.getProjectInfo(pid);
    }
}
