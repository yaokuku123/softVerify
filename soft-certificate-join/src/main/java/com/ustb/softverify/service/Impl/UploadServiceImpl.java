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
        File tmpFile = uploadFile(originFileName,pid,file);
        //数据信息插入表中
        FileUpload fileUploadDb = fileUploadDAO.getFileUpload(pid,fileType);
        if (fileUploadDb != null) {
            //删除原先保存的文件
            FileUtil.delete(fileUploadDb.getFilePath());
            //更新
            fileUploadDb.setFileSize(tmpFile.length());
            fileUploadDb.setFileName(originFileName);
            fileUploadDb.setFilePath(tmpFile.getAbsolutePath());
            fileUploadDb.setPid(pid);
            fileUploadDb.setFileType(fileType);
            fileUploadDAO.updateFileUpload(fileUploadDb);
        } else {
            //插入
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
        //删除本地文件
        FileUpload fileUpload = fileUploadDAO.getFileUpload(pid, fileType);
        FileUtil.delete(fileUpload.getFilePath());
        //删除表数据
        fileUploadDAO.deleteFileUpload(pid,fileType);
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
     *
     * @param file 被测软件
     * @return 上传的文档路径
     */
    private File uploadFile(String fileName,String pid, MultipartFile file) {
        //拼接路径
        String destPath = EnvUtils.ROOT_PATH + Calendar.getInstance().get(Calendar.YEAR) + File.separator + pid
                + File.separator + "origin" + File.separator + fileName;
        //创建文件夹
        File destFile = new File(destPath);
        if (!destFile.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            destFile.getParentFile().mkdirs();
        }
        try {
            //将被测软件保存至本地
            file.transferTo(destFile);
            return destFile;
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

    @Override
    public boolean submitInfo(SoftInfoVo softInfoVo) {
        //插入或更新数据
        SoftInfo softInfoDb = softInfoDAO.getSoftInfo(softInfoVo.getPid());

        if (softInfoDb == null) {
            //插入
            softInfoVo.setUploadPassword(MD5Utils.code(softInfoVo.getUploadPassword()));
            SoftInfo softInfo = new SoftInfo();
            BeanUtils.copyProperties(softInfoVo,softInfo);
            softInfoDAO.insertSoft(softInfo);
        } else {
            //更新
            softInfoDb.setUploadPassword(MD5Utils.code(softInfoVo.getUploadPassword()));
//            softInfoDb.setComName(softInfoVo.getComName());
//            softInfoDb.setProName(softInfoVo.getProName());
            softInfoDAO.updateSoft(softInfoDb);
        }
        //验证路径信息
        String filePath = null;
        List<FileUpload> fileUploadList = fileUploadDAO.listFileUpload(softInfoVo.getPid());
        List<CompInfo> compInfos = new ArrayList<>();
        for (FileUpload fileUpload : fileUploadList) {
            //目录文件，提取目录的存放路径
            if (fileUpload.getFileType().equals(FileTypeEnum.DIR_FILE.getCode())) {
                filePath = fileUpload.getFilePath();
            }
            //重要文件获取，将名称和大小提取至用于对比的对象中
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
    public ProjectVo getResponseInfo(ProjectVo projectVo) {
        SoftInfo softInfoDB = fileUploadDAO.findBySysId(projectVo.getSysId());
        if (softInfoDB == null){
            String pid = UUID.randomUUID().toString();
            projectVo.setPid(pid);
            fileUploadDAO.insertProjectVo(projectVo);
        }

        return projectVo;
    }

    @Override
    public ProjectVo getProjectInfo(String pid) {
        return fileUploadDAO.getProjectInfo(pid);
    }
}
