package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.entity.po.FileTypeEnum;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.StatusEnum;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.BrowserInfoVo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import com.ustb.softverify.entity.vo.SubmitInfoVo;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
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
        Integer fid = uploadService.insertUploadFile(file,pid,fileType);
        return ResponseResult.success().data("fid",fid);
    }

    /**
     * 根据用户标识删除软件关联的文档字段
     * @param govUserId
     * @return
     */
    @GetMapping("/deleteInfo")
    public ResponseResult deleteInfo(@RequestParam("govUserId") Integer govUserId) {
        //删除软件文档信息
        return ResponseResult.success();
    }


    @Transactional
    @GetMapping("/submit")
    public ResponseResult submit(Integer govUserId) {
//        uploadService.updateStatus(govUserId,1);
//        List<FileRecord> fileRecords = fileRecordService.listFileByGovUserId(govUserId);
//        List<CompInfo> compInfos = new ArrayList<>();
//        String filePath = null;
//        for (FileRecord fileRecord : fileRecords) {
//            //目录文件，提取目录的存放路径
//            if (fileRecord.getSoftFileType().equals(FileTypeEnum.DIR_FILE.getCode())) {
//                filePath = fileRecord.getServerLocalPath();
//            }
//            //重要文件获取，将名称和大小提取至用于对比的对象中
//            if (!fileRecord.getSoftFileType().equals(FileTypeEnum.DIR_FILE.getCode()) &&
//                    !fileRecord.getSoftFileType().equals(FileTypeEnum.CONFIG_FILE.getCode())  ) {
//                CompInfo compInfo = new CompInfo();
//                compInfo.setOrgName(fileRecord.getOrgName());
//                compInfo.setFileSize(fileRecord.getFileSize());
//                compInfos.add(compInfo);
//            }
//        }
//        if (filePath == null){
//            throw new CoreFileMisException();
//        }
//        boolean flag = ReadTxt.comp2txt(filePath, compInfos);
//        if (!flag) {
//            throw new MisMatchContentException();
//        }
//        return ResponseResult.success();
        return null;
    }

    /**
     * 获取提交软件的信息
     * @param govUserId
     * @return
     */
    @GetMapping("/submitSoftInfo")
    public ResponseResult submitSoftInfo(@RequestParam("govUserId") Integer govUserId) {
        //根据用户标识和状态信息获取数据（用户数据，软件数据，上传文件数据）
        SubmitInfoVo submitInfo = uploadService.getSubmitInfo(govUserId, StatusEnum.SUMMIT.getCode());
        return ResponseResult.success().data("submitInfo",submitInfo);
    }

    @GetMapping("/browseSoftInfo")
    public ResponseResult browserSoftInfo(@RequestParam("govUserId") Integer govUserId) {
        //根据用户标识和状态信息获取数据（用户数据，软件数据，上传文件数据），获取链上的证书数据
        BrowserInfoVo browseInfo = uploadService.getBrowseInfo(govUserId, StatusEnum.FILED.getCode());
        return ResponseResult.success().data("broseInfo",browseInfo);
    }

}
