package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.VO.PageRequest;
import com.ustb.softverify.entity.VO.PageResult;
import com.ustb.softverify.service.SoftVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@CrossOrigin
public class SoftVerifyController {

    @Autowired
    private SoftVerifyService softVerifyService;

    /**
     * 获取未审核的软件列表信息
     * @param pageQuery 分页查询对象参数
     * @return 查询结果
     */
    @PostMapping("/list")
    public ResponseResult listVerifyInfo(@RequestBody PageRequest pageQuery) {
        PageResult page = softVerifyService.findPage(pageQuery);
        return ResponseResult.success().data("page",page);
    }
}
