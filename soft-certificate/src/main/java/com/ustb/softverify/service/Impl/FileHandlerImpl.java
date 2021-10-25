package com.ustb.softverify.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.ustb.softverify.entity.dto.FileEntity;
import com.ustb.softverify.service.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class FileHandlerImpl implements FileHandler {
    @Override
    public void easyExcelRead(String filePath) {
        EasyExcel.read(filePath, FileEntity.class, new ExcelListener()).sheet().doRead();
    }

    @Override
    public void easyExcelWrite(String filePath,List<FileEntity> fileEntityList) {
        EasyExcel.write(filePath, FileEntity.class).sheet("软件指纹列表清单").doWrite(fileEntityList);
    }

}
