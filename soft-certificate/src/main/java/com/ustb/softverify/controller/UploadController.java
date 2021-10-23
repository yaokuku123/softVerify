package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.entity.po.FileTypeEnum;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.StatusEnum;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.*;
import com.ustb.softverify.exception.*;
import com.ustb.softverify.service.UploadService;
import com.ustb.softverify.utils.ReadTxt;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 插入填写的软件信息
     * @param softInfoVo
     * @return
     */
    @PostMapping("/insertInfo")
    public ResponseResult insertInfo(@RequestBody SoftInfoVo softInfoVo) {
        Integer sid = uploadService.insertSoft(softInfoVo);
        return ResponseResult.success().data("sid",sid);
    }

    /**
     * 更新用户和软件数据信息
     * @param softInfoVo
     * @return
     */
    @PostMapping("/updateInfo")
    public ResponseResult updateInfo(@RequestBody SoftInfoVo softInfoVo) {
        uploadService.updateSoft(softInfoVo);
        return ResponseResult.success();
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
        if (file.getSize() > 1024 * 1024 * 512) {
            throw new CompressSizeException();
        }
        //文档信息插入数据库与文档保存
        uploadService.uploadFile(file,pid,fileType);
        return ResponseResult.success();
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

    @GetMapping("/submit")
    public ResponseResult submit(SoftInfoVo softInfoVo) {
        boolean flag = uploadService.submitInfo(softInfoVo);
        return ResponseResult.success().data("flag",flag);
    }

    /**
     * 获取提交软件的信息
     * @param govUserId
     * @return
     */
//    @GetMapping("/submitSoftInfo")
//    public ResponseResult submitSoftInfo(@RequestParam("govUserId") Integer govUserId) {
//        //根据用户标识和状态信息获取数据（用户数据，软件数据，上传文件数据）
//        SubmitInfoVo submitInfo = uploadService.getSubmitInfo(govUserId, StatusEnum.SUMMIT.getCode());
//        return ResponseResult.success().data("submitInfo",submitInfo);
//    }

    @GetMapping("/browseSoftInfo")
    public ResponseResult browserSoftInfo(@RequestParam("govUserId") Integer govUserId) {
        //根据用户标识和状态信息获取数据（用户数据，软件数据，上传文件数据），获取链上的证书数据
        BrowserInfoVo browseInfo = uploadService.getBrowseInfo(govUserId, StatusEnum.FILED.getCode());
        return ResponseResult.success().data("broseInfo",browseInfo);
    }

}
