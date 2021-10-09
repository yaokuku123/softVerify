package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.SoftInfo;
import com.ustb.softverify.entity.User;
import com.ustb.softverify.entity.VO.UserUploadInfoVo;
import com.ustb.softverify.service.SoftInfoService;
import com.ustb.softverify.service.UserService;
import com.ustb.softverify.utils.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * @author WYP
 * @date 2021-10-08 15:52
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private SoftInfoService softInfoService;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestPart("files") MultipartFile[] files,@RequestPart("userUploadInfoVO") UserUploadInfoVo userUploadInfo){
        if (files.length != 2){
            return ResponseResult.error().message("文件个数错误");
        }
        String soft = userUploadInfo.getSoftName();
        String doc = userUploadInfo.getSoftName();
        String softSuffix = soft.substring(soft.lastIndexOf("."));
        String docSuffix = doc.substring(doc.lastIndexOf("."));

        if (!".rar".equals(softSuffix) && !".zip".equals(softSuffix) ||!".xlsx".equals(docSuffix)){
            return ResponseResult.error().message("上传文件类型错误");
        }

        User user = new User();
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfo,user);
        userService.insertUser(user);
        //保存文件
        String filePath = System.getProperty("user.dir") + "/data/" + user.getUname() + "/" + softInfo.getSoftName() + "/";
        //FileUtil.saveFiles(userUploadInfo,filePath);
        String softDestPath = filePath + user.getUname() + "/" + softInfo.getSoftName() + "/" + softSuffix;
        String docDestPath = filePath + user.getUname() + "/" + softInfo.getSoftName() + "/" + docSuffix;
        File fileDir = new File(filePath);
        fileDir.mkdirs();
        File softDestFile = new File(softDestPath);
        File docDestFile = new File(docDestPath);
        try {
            files[0].transferTo(softDestFile);
            files[1].transferTo(docDestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //存储软件相关信息
        BeanUtils.copyProperties(userUploadInfo,softInfo);
        softInfo.setSoftPath(filePath+soft).setDocPath(filePath+doc).setStatus(0).setUser(user);
        softInfoService.insertSoft(softInfo);

        return ResponseResult.success().data("user",user).data("softInfo",softInfo);
    }


}
