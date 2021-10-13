package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.vo.PageRequest;
import com.ustb.softverify.entity.vo.PageResult;
import com.ustb.softverify.service.SoftVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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

    /**
     * 下载指定用户标识下的软件压缩包
     * @param govUserId 用户标识
     * @param softName 软件名称
     * @param request
     * @param response
     * @return 下载的软件
     */
    @GetMapping("/download")
    public ResponseResult downloadFile(@RequestParam("govUserId") Integer govUserId,
                               @RequestParam("softName") String softName,
                               HttpServletRequest request, HttpServletResponse response) {
        //查询软件路径
        String softPath = softVerifyService.getSoftPath(govUserId, softName);
        //下载软件
        File file = new File(softPath);
        // 设置下载软件文件名
        String fileName = softPath.substring(softPath.lastIndexOf("/") + 1);
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ResponseResult.success().message("下载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error().message("下载失败");
    }

    /**
     * 审核通过
     * @param govUserId 用户标识
     * @param softName 软件名称
     * @return
     */
    @GetMapping("/success")
    public ResponseResult verifySuccess(@RequestParam("govUserId") Integer govUserId,
                                        @RequestParam("softName") String softName) {
        softVerifyService.verifySuccess(govUserId,softName);
        return ResponseResult.success().message("审核通过");
    }

    /**
     * 审核驳回
     * @param govUserId 用户标识
     * @param softName 软件名称
     * @return
     */
    @GetMapping("fail")
    public ResponseResult verifyFail(@RequestParam("govUserId") Integer govUserId,
                                     @RequestParam("softName") String softName) {
        softVerifyService.verifyFail(govUserId,softName);
        return ResponseResult.success().message("审核驳回");
    }

    /**
     * 获取已审核的软件列表信息
     * @param pageQuery 分页查询对象参数
     * @return 查询结果
     */
    @PostMapping("/list/success")
    public ResponseResult listVerifySuccess(@RequestBody PageRequest pageQuery) {
        PageResult page = softVerifyService.findPageSuccess(pageQuery);
        return ResponseResult.success().data("page",page);
    }

    /**
     * 获取审核驳回的软件列表信息
     * @param pageQuery 分页查询对象参数
     * @return 查询结果
     */
    @PostMapping("/list/fail")
    public ResponseResult listVerifyFail(@RequestBody PageRequest pageQuery) {
        PageResult page = softVerifyService.findPageFail(pageQuery);
        return ResponseResult.success().data("page",page);
    }
}
