package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.po.FileUpload;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.vo.ProjectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FileUploadDAO {
    /**
     * 插入fileUpload的数据
     * @param fileUpload
     * @return
     */
    Integer insertFileUpload(FileUpload fileUpload);

    /**
     * 获取全部pid绑定的文档数据
     * @param pid
     * @return
     */
    List<FileUpload> listFileUpload(@Param("pid") String pid);

    /**
     * 根据pid和文件类型获取数据信息
     * @param pid
     * @param fileType
     * @return
     */
    FileUpload getFileUpload(@Param("pid") String pid, @Param("fileType") Integer fileType);

    /**
     * 更新文件文档信息
     * @param fileUploadDb
     */
    void updateFileUpload(FileUpload fileUploadDb);

    /**
     * 删除表数据
     * @param pid
     * @param fileType
     */
    void deleteFileUpload(@Param("pid") String pid, @Param("fileType") Integer fileType);

    void updateStatus(String pid);

    /**
     * 根据sysId查询是否存在数据
     * @param sysId
     * @return
     */
    SoftInfo findBySysId(@Param("sysId") String sysId);

    /**
     * 保存传递过来的信息
     * @param projectVo
     * @return
     */
    void insertProjectVo(ProjectVo projectVo);


    SoftInfo getProjectInfo(@Param("pid") String pid);

}
