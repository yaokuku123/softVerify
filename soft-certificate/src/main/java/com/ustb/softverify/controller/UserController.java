package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.vo.UserVo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    //login
    @PostMapping("login")
    public ResponseResult login(@RequestBody UserVo userVo) {
        System.out.println(userVo);
        if ("admin".equals(userVo.getUsername()) && "bjsjxj-bjkjdx-admin".equals(userVo.getPassword())) {
            return ResponseResult.success().data("token","admin");
        }
        if ("guest".equals(userVo.getUsername()) && "bjsjxj-bjkjdx-guest".equals(userVo.getPassword())) {
            return ResponseResult.success().data("token","admin");
        }
        if ("manager".equals(userVo.getUsername()) && "bjsjxj-bjkjdx-manager".equals(userVo.getPassword())) {
            return ResponseResult.success().data("token","admin");
        }
        throw new RuntimeException("error login");
    }

    //info
    @GetMapping("info")
    public ResponseResult info(String token) {
        System.out.println(token);
        return ResponseResult.success().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
