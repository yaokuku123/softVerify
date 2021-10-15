package com.ustb.softverify.service;

import com.ustb.softverify.entity.dto.FileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WYP
 * @date 2021-10-14 10:29
 */
public interface SoftUploadService {

    User insertUser(UserUploadInfoVo userUploadInfo);

    User getUser(Integer govUserId);

    void insertSoft(SoftInfo softInfo);

    SoftInfo getSoftByUIdAndName(Integer uid, String softName);

    void insert(SignFile signFile);

    // 保存文件
    void saveFile(MultipartFile[] files, UserUploadInfoVo userUploadInfoVo, FileInfo fileInfo);

    // 压缩 合法性验证
    void verifyAndSave(FileInfo fileInfo,SoftInfo softInfo,UserUploadInfoVo userUploadInfo,User user) throws Exception;

    void clear(Integer uid,String softName);

}
