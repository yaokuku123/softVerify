package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.entity.po.FileTypeEnum;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.StatusEnum;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.BrowserInfoVo;
import com.ustb.softverify.entity.vo.SubmitInfoVo;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.exception.CoreFileMisException;
import com.ustb.softverify.exception.MisMatchContentException;
import com.ustb.softverify.service.UploadService;
import com.ustb.softverify.utils.ReadTxt;
import com.ustb.softverify.webupload.entity.FileRecord;
import com.ustb.softverify.webupload.service.IFileRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private IFileRecordService fileRecordService;

    /**
     * 根据用户标识查找软件状态信息
     * @param govUserId
     * @return
     */
    @GetMapping("/getStatus")
    public ResponseResult getStatus(@RequestParam("govUserId") Integer govUserId) {

        //获取用户信息
        User user = uploadService.getUser(govUserId);
        //获取软件信息
        List<SoftInfo> softInfos = uploadService.listSoft(govUserId);
        if (user == null || softInfos.size() == 0 ) {
            return ResponseResult.success().data("status",-1);
        }
        for (SoftInfo softInfo : softInfos) {
            if (softInfo.getStatus() == 0) {
                UserUploadInfoVo userUploadInfo = new UserUploadInfoVo();
                BeanUtils.copyProperties(softInfo,userUploadInfo);
                BeanUtils.copyProperties(user,userUploadInfo);
                return ResponseResult.success().data("status",0).data("userUploadInfo",userUploadInfo);
            }
            if (softInfo.getStatus() == 1) {
                UserUploadInfoVo userUploadInfo = new UserUploadInfoVo();
                BeanUtils.copyProperties(softInfo,userUploadInfo);
                BeanUtils.copyProperties(user,userUploadInfo);
                return ResponseResult.success().data("status",1).data("userUploadInfo",userUploadInfo);
            }
            if (softInfo.getStatus() == 2) {
                return ResponseResult.success().data("status",2);
            }
        }
        return ResponseResult.success().data("status",3);
    }

    /**
     * 根据用户标识删除软件关联的文档字段
     * @param govUserId
     * @return
     */
    @GetMapping("/deleteInfo")
    public ResponseResult deleteInfo(@RequestParam("govUserId") Integer govUserId) {
        //删除软件文档信息
        fileRecordService.delFileByGovUserId(govUserId);
        return ResponseResult.success();
    }

    /**
     * 插入用户和软件数据信息
     * @param userUploadInfoVo
     * @return
     */
    @Transactional
    @PostMapping("/insertInfo")
    public ResponseResult insertInfo(@RequestBody UserUploadInfoVo userUploadInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userUploadInfoVo,user);
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfoVo,softInfo);
        //插入用户信息
        //如果不是第一次上传，需要先查看是否有数据
        User userDb = uploadService.getUser(userUploadInfoVo.getGovUserId());
        if (userDb != null) {
            userDb.setCompany(user.getCompany());
            userDb.setGovUserId(user.getGovUserId());
            userDb.setPhone(user.getPhone());
            userDb.setUname(user.getUname());
            user.setUid(userDb.getUid());
            uploadService.updateUser(userDb);
        } else {
            uploadService.insertUser(user);
        }
        //插入软件信息
        uploadService.insertSoft(softInfo);
        Integer uid = user.getUid();
        Integer sid = softInfo.getSid();
        return ResponseResult.success().data("uid",uid).data("sid",sid);
    }

    /**
     * 更新用户和软件数据信息
     * @param userUploadInfoVo
     * @return
     */
    @Transactional
    @PostMapping("/updateInfo")
    public ResponseResult updateInfo(@RequestBody UserUploadInfoVo userUploadInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userUploadInfoVo,user);
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(userUploadInfoVo,softInfo);
        //更新用户信息
        uploadService.updateUser(user);
        //更新软件信息
        uploadService.updateSoft(softInfo);
        return ResponseResult.success();
    }

    /**
     * 保存当前文件信息
     * @param govUserId
     */
    @GetMapping("/save")
    public ResponseResult save(Integer govUserId) {
        uploadService.updateStatus(govUserId,0);
        return ResponseResult.success();
    }

    @Transactional
    @GetMapping("/submit")
    public ResponseResult submit(Integer govUserId) {
        uploadService.updateStatus(govUserId,1);
        List<FileRecord> fileRecords = fileRecordService.listFileByGovUserId(govUserId);
        List<CompInfo> compInfos = new ArrayList<>();
        String filePath = null;
        for (FileRecord fileRecord : fileRecords) {
            //目录文件，提取目录的存放路径
            if (fileRecord.getSoftFileType().equals(FileTypeEnum.DIR_FILE.getCode())) {
                filePath = fileRecord.getServerLocalPath();
            }
            //重要文件获取，将名称和大小提取至用于对比的对象中
            if (!fileRecord.getSoftFileType().equals(FileTypeEnum.DIR_FILE.getCode()) &&
                    !fileRecord.getSoftFileType().equals(FileTypeEnum.CONFIG_FILE.getCode())  ) {
                CompInfo compInfo = new CompInfo();
                compInfo.setOrgName(fileRecord.getOrgName());
                compInfo.setFileSize(fileRecord.getFileSize());
                compInfos.add(compInfo);
            }
        }
        if (filePath == null){
            throw new CoreFileMisException();
        }
        boolean flag = ReadTxt.comp2txt(filePath, compInfos);
        if (!flag) {
            throw new MisMatchContentException();
        }
        return ResponseResult.success();
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
