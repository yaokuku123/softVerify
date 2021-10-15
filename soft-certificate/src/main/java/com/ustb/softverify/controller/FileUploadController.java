package com.ustb.softverify.controller;



import com.ustb.softverify.algorithm.sm3.SM3Algorithm;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.FileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.service.Impl.ControlExcelImpl;
import com.ustb.softverify.service.Impl.ZipCompressImpl;
import com.ustb.softverify.service.SoftUploadService;
import com.ustb.softverify.utils.EnvUtils;
import com.ustb.softverify.utils.FileUtil;
import com.ustb.softverify.utils.HashBasicOperaterSetUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author WYP
 * @date 2021-10-08 15:52
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileUploadController {

    @Autowired
    private ZipCompressImpl zipCompress;

    @Autowired
    private ControlExcelImpl controlExcel;

    @Autowired
    private SoftUploadService softUploadService;


    @PostMapping("/upload")
    public ResponseResult upload(@RequestPart("files") MultipartFile[] files,@RequestPart("userUploadInfoVO") UserUploadInfoVo userUploadInfo) throws Exception {
        if (files.length != 2){
            return ResponseResult.error().message("文件个数错误");
        }
        if (files[0].getSize() > 1024 * 1024 * 500 && files[1].getSize() > 1024 * 1024 * 500){
            return ResponseResult.error().message("上传文件过大~");
        }
        String soft = userUploadInfo.getSoftName();
        String originalFileName = files[0].getOriginalFilename();
        String originalDocName = files[1].getOriginalFilename();
        String softSuffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String docSuffix = originalDocName.substring(originalDocName.lastIndexOf("."));

        if (!".rar".equals(softSuffix) && !".zip".equals(softSuffix) ||!".xlsx".equals(docSuffix)){
            return ResponseResult.error().message("上传文件类型错误");
        }

        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfo,softInfo);

        User user = softUploadService.insertUser(userUploadInfo);

        String softName = user.getGovUserId() + "-" + soft + softSuffix;
        String docName = user.getGovUserId() + "-" + soft + docSuffix;
        String filePath = EnvUtils.ROOT_PATH + userUploadInfo.getGovUserId() + "/" + softInfo.getSoftName() + "/";
        FileInfo fileInfo = new FileInfo();
        fileInfo.setSoftName(softName).setDocName(docName).setFilePath(filePath);

        softUploadService.saveFile(files,userUploadInfo,fileInfo);
        softUploadService.verifyAndSave(fileInfo,softInfo,userUploadInfo,user);

        return ResponseResult.success();
    }
}
