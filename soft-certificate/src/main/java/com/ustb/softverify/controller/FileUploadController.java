package com.ustb.softverify.controller;



import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.algorithm.sm3.SM3Algorithm;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.SignFile;
import com.ustb.softverify.entity.SoftInfo;
import com.ustb.softverify.entity.User;
import com.ustb.softverify.entity.VO.UserUploadInfoVo;
import com.ustb.softverify.service.Impl.ControlExcelImpl;
import com.ustb.softverify.service.Impl.ZipCompressImpl;
import com.ustb.softverify.service.SignFileService;
import com.ustb.softverify.service.SoftInfoService;
import com.ustb.softverify.service.UserService;
import com.ustb.softverify.utils.FileUtil;
import com.ustb.softverify.utils.HashBasicOperaterSetUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FileUploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private SoftInfoService softInfoService;

    @Autowired
    private ZipCompressImpl zipCompress;

    @Autowired
    private ControlExcelImpl controlExcel;

    @Autowired
    private SignFileService signFileService;

//    @Autowired
//    private FileTransferUtil fileTransferUtil;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestPart("files") MultipartFile[] files,@RequestPart("userUploadInfoVO") UserUploadInfoVo userUploadInfo) throws Exception {
        if (files.length != 2){
            return ResponseResult.error().message("文件个数错误");
        }
        long size = files[0].getSize();
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

        User user = new User();
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfo,user);
        BeanUtils.copyProperties(userUploadInfo,softInfo);
        userService.insertUser(user);
        //保存文件
        String softName = user.getUname() + "-" + soft + softSuffix;
        String docName = user.getUname() + "-" + soft + docSuffix;
        String filePath = System.getProperty("user.dir") + "/data/" + user.getUname() + "/" + softInfo.getSoftName() + "/";

        //判断当前文件下是否有文件
        File currentFile = new File(filePath);
        currentFile.mkdirs();
        if (currentFile.list().length > 0 ){
            FileUtil.deleteDir(filePath);
        }


        String softDestPath = filePath + softName;
        String docDestPath = filePath + docName;
        File softDestFile = new File(softDestPath);
        File docDestFile = new File(docDestPath);
        try {
            files[0].transferTo(softDestFile);
            files[1].transferTo(docDestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 本地 合法性验证
        String unzipName = zipCompress.unzip(softDestPath, filePath);
        String unzipFilePath = filePath + unzipName;

        // 修改文件为只读
       zipCompress.changeroot(unzipFilePath);

        List<Map<String, String>> maps = controlExcel.redExcel(docDestPath);
        List<String> excelPaths = new ArrayList<>();
        for (Map<String, String> map : maps){
            excelPaths.add(map.get("文件路径（相对路径）"));
        }

        ArrayList<File> allFiles = FileUtil.getAllFiles(unzipFilePath);
        ArrayList<String> allFilesPath = new ArrayList<>();
        for (File file : allFiles) {
            //file replace
            allFilesPath.add(file.getAbsolutePath().replace("\\","/"));
        }

        for (String s : excelPaths){
            String s1 = filePath + s;
            if (!allFilesPath.contains(s1.replace("\\", "/"))){
                return ResponseResult.error().message("文件路径错误");
            }
        }


        // 验证正确   将 hash 和 excel路径 存进数据库
        String hash = HashBasicOperaterSetUtil.byteToHex(SM3Algorithm.SM3Encrypt(softDestPath));

        //存储软件相关信息
        softInfo.setSoftName(soft).setSoftPath(softDestPath).setDocPath(docDestPath).setStatus(0).setUser(user).setHash(hash);
        softInfoService.insertSoft(softInfo);

        // 存放读取正确路径
        SignFile signFile = new SignFile();
        signFile.setSoftInfo(softInfo);
        for (String s : excelPaths){
            signFile.setPath(s);
            signFileService.insert(signFile);
        }

        return ResponseResult.success().data("paths",excelPaths).data("allFilesPath",allFilesPath);
    }
}
