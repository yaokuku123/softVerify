package com.ustb.softverify.service;

import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.BrowserInfoVo;
import com.ustb.softverify.entity.vo.InfoBackVo;
import com.ustb.softverify.entity.vo.ProjectVo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {



    /**
     * 根据用户标识获取软件列表
     * @param govUserId 用户标识
     * @return 软件列表信息
     */
    List<SoftInfo> listSoft(Integer govUserId);



    void updateSoft(SoftInfoVo softInfo);

    /**
     * 更新软件状态
     * @param govUserId 用户标识
     * @param status 当前状态
     */
    void updateStatus(Integer govUserId, Integer status);


    /**
     * 保存上传文档和信息
     * @return
     */
     void uploadFile(MultipartFile file, String pid, Integer fileType);

    /**
     * 获取回显信息
     * @param pid
     * @return
     */
    InfoBackVo getInfo(String pid);

    /**
     * 删除文档
     * @param pid
     * @param fileType
     */
    void deleteFile(String pid, Integer fileType);

    /**
     * 提交软件信息
     * @param softInfoVo
     */
    boolean submitInfo(SoftInfoVo softInfoVo);

    /**
     * 获取文档的上传路径
     * @param pid
     * @param fileType
     * @return
     */
    String getUploadFilePath(String pid, Integer fileType);

    /**
     * 返回数据
     * @param
     * @return
     */
    SoftInfoVo getResponseInfo(ProjectVo projectVo);

    SoftInfo getProjectInfo(String pid);
}
