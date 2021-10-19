package com.ustb.softverify.webupload.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ustb.softverify.common.entity.Result;
import com.ustb.softverify.webupload.entity.FileRecord;
import com.ustb.softverify.webupload.entity.FileZoneRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 文件上传记录 服务类
 * </p>
 *
 * @author haijun
 * @since 2020-02-14
 */
public interface IFileRecordService extends IService<FileRecord> {

    Result upload(HttpServletRequest request, Integer uploadType, Integer storageYear);

    Result zoneUpload(HttpServletRequest request, String contentType, FileZoneRecord fileZoneRecord);

    Result md5Check(FileZoneRecord fileZoneRecord, Integer checkType, String contentType, HttpServletRequest request);

    Result mergeZoneFile(String totalmd5, HttpServletRequest request);

    Result delZoneFile(String totalmd5);

    Result delFile(String fileId);

    /**
     * 根据用户标识删除软件文档
     * @param govUserId
     * @return
     */
    void delFileByGovUserId(Integer govUserId);

    /**
     * 根据用户标识获取软件文件列表对象
     * @param govUserId
     * @return
     */
    List<FileRecord> listFileByGovUserId(Integer govUserId);
//    void recordDownloadLog(String fileId, FileRecord fileRecord);
}
