package com.ustb.softverify.webupload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ustb.softverify.webupload.entity.FileZoneRecord;

import java.util.List;

/**
 * <p>
 * 文件分片记录 服务类
 * </p>
 *
 * @author haijun
 * @since 2020-02-14
 */
public interface IFileZoneRecordService extends IService<FileZoneRecord> {

    FileZoneRecord selByMD5AndZoneTotalMd5(String zoneMd5, String zoneTotalMd5);

    List<FileZoneRecord> selByTotalMd5(String totalmd5);
}
