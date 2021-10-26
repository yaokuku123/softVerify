package com.ustb.softverify.service;

import com.ustb.softverify.entity.dto.FileEntity;

import java.util.List;

public interface FileHandler {

    // 读文件
    void easyExcelRead(String filePath);

    // 写文件
    void easyExcelWrite(String filePath, List<FileEntity> fileEntityList);
}
