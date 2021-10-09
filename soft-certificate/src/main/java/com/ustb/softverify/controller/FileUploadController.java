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
        String soft = files[0].getOriginalFilename();
        String doc = files[1].getOriginalFilename();
        String softSuffix = soft.substring(soft.lastIndexOf("."), soft.length());
        String docSuffix = doc.substring(doc.lastIndexOf("."), doc.length());
        if (!".rar".equals(softSuffix) && !".zip".equals(softSuffix) ||!".xlsx".equals(docSuffix)){
            return ResponseResult.error().message("上传文件类型错误");
        }

        User user = new User();
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfo,user);
        softInfo.setSoftName(userUploadInfo.getSoftName()).setSoftDesc(userUploadInfo.getSoftDesc());
        userService.insertUser(user);
        //保存文件
        String filePath = System.getProperty("user.dir") + "/data/" + user.getUname() + "/" + softInfo.getSoftName() + "/";
        FileUtil.saveFiles(files,filePath);

        //存储软件相关信息
        BeanUtils.copyProperties(userUploadInfo,softInfo);
        softInfo.setSoftPath(filePath+soft).setDocPath(filePath+doc).setStatus(0).setUser(user);
        softInfoService.insertSoft(softInfo);

        return ResponseResult.success().data("user",user).data("softInfo",softInfo);
    }


}
