package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.vo.UserVo;
import com.ustb.softverify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

//    //login
//    @PostMapping("login")
//    public ResponseResult login(@RequestBody UserVo userVo) {
//        System.out.println(userVo);
//        return ResponseResult.success().data("token","admin");
//    }
//
//    //info
//    @GetMapping("info")
//    public ResponseResult info(String token) {
//        System.out.println(token);
//        return ResponseResult.success().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//    }

    @PostMapping("login")
    public ResponseResult login(@RequestBody UserVo userVo) {
        System.out.println(userVo);
        boolean flag = userService.login(userVo);
        return ResponseResult.success().data("flag",flag);
    }
}
