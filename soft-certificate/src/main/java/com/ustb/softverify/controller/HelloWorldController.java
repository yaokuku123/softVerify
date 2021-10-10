package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.utils.FileTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class HelloWorldController {

    @Autowired
    private FileTransferUtil fileTransferUtil;
    @GetMapping("/hello")
    public ResponseResult sayHello() {

        fileTransferUtil.transfer("D:\\1.jpg","/root/");
        return ResponseResult.success().data("success","传输成功");

    }
}
