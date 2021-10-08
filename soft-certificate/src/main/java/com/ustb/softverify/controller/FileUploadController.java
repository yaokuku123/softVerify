package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.User;
import com.ustb.softverify.entity.vo.UserUploadInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WYP
 * @date 2021-10-08 15:52
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("files") MultipartFile[] files, @RequestBody UserUploadInfo userUploadInfo){
        if (files.length != 2){
            return ResponseResult.error().message("文件个数错误");
        }
        User user = new User();
        BeanUtils.copyProperties(userUploadInfo,user);



        return ResponseResult.success().data("user",user);
    }
}
