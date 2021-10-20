package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.service.UploadService;
import com.ustb.softverify.utils.ReadTxt;
import com.ustb.softverify.webupload.entity.FileRecord;
import com.ustb.softverify.webupload.service.IFileRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private IFileRecordService fileRecordService;

    /**
     * 根据用户标识查找软件状态信息
     * @param govUserId
     * @return
     */
    @GetMapping("/getStatus")
    public ResponseResult getStatus(@RequestParam("govUserId") Integer govUserId) {
        List<SoftInfo> softInfos = null;
        User user = null;
        try {
            //获取用户信息
            user = uploadService.getUser(govUserId);
            //获取软件信息
            softInfos = uploadService.listSoft(govUserId);
        } catch (Exception e) {
            return ResponseResult.success().data("status",-1);
        }
        for (SoftInfo softInfo : softInfos) {
            if (softInfo.getStatus() == 0) {
                UserUploadInfoVo userUploadInfo = new UserUploadInfoVo();
                BeanUtils.copyProperties(softInfo,userUploadInfo);
                BeanUtils.copyProperties(user,userUploadInfo);
                return ResponseResult.success().data("status",0).data("userUploadInfo",userUploadInfo);
            }
            if (softInfo.getStatus() == 1) {
                UserUploadInfoVo userUploadInfo = new UserUploadInfoVo();
                BeanUtils.copyProperties(softInfo,userUploadInfo);
                BeanUtils.copyProperties(user,userUploadInfo);
                return ResponseResult.success().data("status",1).data("userUploadInfo",userUploadInfo);
            }
            if (softInfo.getStatus() == 2) {
                return ResponseResult.success().data("status",2);
            }
        }
        return ResponseResult.success().data("status",3);
    }

    /**
     * 根据用户标识删除软件关联的文档字段
     * @param govUserId
     * @return
     */
    @GetMapping("/deleteInfo")
    public ResponseResult deleteInfo(@RequestParam("govUserId") Integer govUserId) {
        //删除软件文档信息
        fileRecordService.delFileByGovUserId(govUserId);
        return ResponseResult.success();
    }

    /**
     * 插入用户和软件数据信息
     * @param userUploadInfoVo
     * @return
     */
    @Transactional
    @PostMapping("/insertInfo")
    public ResponseResult insertInfo(@RequestBody UserUploadInfoVo userUploadInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userUploadInfoVo,user);
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfoVo,softInfo);
        //插入用户信息
        uploadService.insertUser(user);
        //插入软件信息
        uploadService.insertSoft(softInfo);
        return ResponseResult.success();
    }

    /**
     * 更新用户和软件数据信息
     * @param userUploadInfoVo
     * @return
     */
    @Transactional
    @PostMapping("/updateInfo")
    public ResponseResult updateInfo(@RequestBody UserUploadInfoVo userUploadInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userUploadInfoVo,user);
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfoVo,softInfo);
        //更新用户信息
        uploadService.updateUser(user);
        //更新软件信息
        uploadService.updateSoft(softInfo);
        return ResponseResult.success();
    }

    /**
     * 保存当前文件信息
     * @param govUserId
     */
    @GetMapping("/save")
    public ResponseResult save(Integer govUserId) {
        uploadService.updateStatus(govUserId,0);
        return ResponseResult.success();
    }

    @Transactional
    @GetMapping("/submit")
    public ResponseResult submit(Integer govUserId) {
        uploadService.updateStatus(govUserId,1);
        List<FileRecord> fileRecords = fileRecordService.listFileByGovUserId(govUserId);
        List<CompInfo> compInfos = new ArrayList<>();
        String filePath = null;
        for (FileRecord fileRecord : fileRecords) {
            if ("CoreFiles.txt".equals(fileRecord.getOrgName())) {
                filePath = fileRecord.getServerLocalPath();
                continue;
            }
            CompInfo compInfo = new CompInfo();
            compInfo.setOrgName(fileRecord.getOrgName());
            compInfo.setFileSize(fileRecord.getFileSize());
            compInfos.add(compInfo);
        }
        boolean flag = ReadTxt.comp2txt(filePath, compInfos);
        if (!flag) {
            return ResponseResult.error();
        }
        return ResponseResult.success();
    }
}
