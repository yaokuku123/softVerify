package com.ustb.softverify.service.Impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ustb.softverify.entity.dto.FileEntity;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<FileEntity> {

    //读取除表头外的每行数据执行该方法
    @Override
    public void invoke(FileEntity fileEntity, AnalysisContext analysisContext) {
        System.out.println(fileEntity);
    }

    //获取表头数据
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println(headMap);
    }

    //读取结束执行该方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("finish");
    }
}
