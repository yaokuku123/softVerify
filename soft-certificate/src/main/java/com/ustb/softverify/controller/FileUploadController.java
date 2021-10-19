package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.FileInfo;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.exception.CompressFormatException;
import com.ustb.softverify.exception.CompressNumException;
import com.ustb.softverify.exception.CompressSizeException;
import com.ustb.softverify.exception.UploaderInfoException;
import com.ustb.softverify.service.SoftUploadService;
import com.ustb.softverify.utils.EnvUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Field;



/**
 * @author WYP
 * @date 2021-10-08 15:52
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileUploadController {

    @Autowired
    private SoftUploadService softUploadService;


    @PostMapping("/upload")
    public ResponseResult upload(@RequestPart("files") MultipartFile[] files,
                                 @RequestPart("userUploadInfoVO") UserUploadInfoVo userUploadInfo) throws IllegalAccessException {
        for (Field f : userUploadInfo.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(userUploadInfo) == null) {
                throw new UploaderInfoException();
            }
        }
        if (files[0].getSize() > 1024 * 1024 * 1000 && files[1].getSize() > 1024 * 1024 * 1000){
            throw new CompressSizeException();
        }
        String soft = userUploadInfo.getSoftName();
        String originalFileName = files[0].getOriginalFilename();
        String originalDocName = files[1].getOriginalFilename();
        String softSuffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String docSuffix = originalDocName.substring(originalDocName.lastIndexOf("."));

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
