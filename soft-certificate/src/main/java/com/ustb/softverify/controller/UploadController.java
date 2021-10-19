package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfo;
import com.ustb.softverify.service.UploadService;
import org.apache.ibatis.executor.ExecutorException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;

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
                return ResponseResult.success().data("status",0);
            }
            if (softInfo.getStatus() == 1) {
                UserUploadInfo userUploadInfo = new UserUploadInfo();
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
}
