package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.po.FileUpload;

public interface FileUploadDAO {
    /**
     * 插入fileUpload的数据
     * @param fileUpload
     * @return
     */
    Integer insertFileUpload(FileUpload fileUpload);
}
