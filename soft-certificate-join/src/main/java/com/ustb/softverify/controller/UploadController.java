package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.vo.InfoBackVo;
import com.ustb.softverify.entity.vo.ProjectVo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import com.ustb.softverify.exception.CompressNumException;
import com.ustb.softverify.exception.CompressSizeException;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;


    /**
     * 更新用户的口令和状态信息
     * @param softInfoVo
     * @return
     */
    @PostMapping("/updateInfo")
    public ResponseResult updateInfo(@RequestBody SoftInfoVo softInfoVo) {
        uploadService.updateSoft(softInfoVo);
        return ResponseResult.success();
    }


    /**
     * 文件文档上传
     * @param file
     * @param pid
     * @param fileType
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("pid") String pid,
                                 @RequestParam("fileType") Integer fileType)  {
        //边界判定
        if (file.isEmpty()) {
            throw new CompressNumException();
        }
        if (file.getSize() > 1024 * 1024 * 200) {
            throw new CompressSizeException();
        }
        //文档信息插入数据库与文档保存
        uploadService.uploadFile(file,pid,fileType);
        return ResponseResult.success();
    }

    /**
     * 下载文件
     * @param pid
     * @param fileType
     * @param response
     * @return
     */
    @GetMapping(value = "/download",produces = "application/json;charset=UTF-8")
    public ResponseResult download(@RequestParam("pid")String pid,
                                      @RequestParam("fileType") Integer fileType,
                                      HttpServletResponse response){
        //查找文档保存路径
        String filePath = uploadService.getUploadFilePath(pid,fileType);
        //下载软件
        File file = new File(filePath);
        // 设置下载软件文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1));// 设置文件名
        OutputStream os = null;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ResponseResult.success().message("下载成功");
        } catch (Exception e) {
            throw new FileReadWriteException();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                throw new FileReadWriteException();
            }
        }
    }

    /**
     * 删除上传文件
     * @param pid
     * @param fileType
     * @return
     */
    @GetMapping("/deleteFile")
    public ResponseResult deleteInfo(@RequestParam("pid") String pid,
                                     @RequestParam("fileType") Integer fileType) {
        //删除软件文档信息
        uploadService.deleteFile(pid,fileType);

        return ResponseResult.success();
    }

    /**
     * 提交数据请求
     * @param softInfoVo
     * @return
     */
    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody SoftInfoVo softInfoVo) {
        boolean flag = uploadService.submitInfo(softInfoVo);
        return ResponseResult.success().data("flag",flag);
    }


    /**
     * 接收全流程系统发送的数据
     * @param projectVo
     * @return
     */
    @PostMapping("/softwareaudit")
    public ResponseResult softwareaudit(@RequestBody ProjectVo projectVo){
        SoftInfoVo softInfo = uploadService.getResponseInfo(projectVo);
        System.out.println(softInfo);
        return ResponseResult.success().data("softInfo",softInfo);
    }


    /**
     * 获取数据信息
     * @param pid
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseResult getInfo(@RequestParam("pid") String pid) {
        InfoBackVo infoBackVo = uploadService.getInfo(pid);
        return ResponseResult.success().data("softInfo",infoBackVo);
    }

}
