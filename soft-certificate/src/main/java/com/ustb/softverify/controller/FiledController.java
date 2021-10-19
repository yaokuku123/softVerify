package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.vo.UserSoftInfoVo;
import com.ustb.softverify.service.SoftInfoService;
import com.ustb.softverify.service.SoftUploadService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 14:31
 */
@RestController
public class FiledController {

    @Autowired
    private SoftInfoService softInfoService;


    @GetMapping("/userFileInfo")
    public ResponseResult getUserUploadInfo(@RequestParam("govUserId") Integer govUserId){
        List<UserSoftInfoVo> uploadInfo = softInfoService.getUploadInfo(govUserId);
        return ResponseResult.success().data("softInfo",uploadInfo);

    }


}
