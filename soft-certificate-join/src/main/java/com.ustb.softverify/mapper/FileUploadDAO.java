package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.po.FileUpload;
import org.apache.ibatis.annotations.Param;

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
    FileUpload getFileUpload(@Param("pid") String pid,@Param("fileType") Integer fileType);

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
    void deleteFileUpload(@Param("pid") String pid,@Param("fileType") Integer fileType);

    void updateStatus(String pid);

}
