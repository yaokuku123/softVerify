package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.SoftInfo;
import com.ustb.softverify.entity.User;
import com.ustb.softverify.entity.VO.UserUploadInfoVo;
import com.ustb.softverify.service.UserService;
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

    @PostMapping("/upload")
    public ResponseResult upload(@RequestPart("files") MultipartFile[] files,@RequestPart("userUploadInfoVO") UserUploadInfoVo userUploadInfo){
        if (files.length != 2){
            return ResponseResult.error().message("文件个数错误");
        }
        User user = new User();
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfo,user);
        softInfo.setName(userUploadInfo.getSoftName()).setDesc(userUploadInfo.getSoftDesc());

        userService.insertUser(user);


        return ResponseResult.success().data("user",user).data("softInfo",softInfo);
    }


}
