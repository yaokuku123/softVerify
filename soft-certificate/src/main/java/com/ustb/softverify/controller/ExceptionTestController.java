package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.exception.ParamNullException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
@CrossOrigin
public class ExceptionTestController {

    @GetMapping("/paramNullException")
    public ResponseResult paramNullException() {
        throw new ParamNullException("参数错误");
    }
}
