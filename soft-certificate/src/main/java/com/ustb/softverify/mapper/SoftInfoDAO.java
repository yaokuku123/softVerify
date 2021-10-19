package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 14:33
 */
public interface SoftInfoDAO {

    /**
     * 获取用户以上传列表信息
     * @param govUserId
     * @return
     */
    List<SoftInfoVo> getUploadInfo(Integer govUserId);



    /**
     * 查询所有已保存待归档软件列表信息
     * @return
     */
    List<SoftInfoVo> getUnFiledSoftInfo();


    /**
     * 查询所有以归档软件列表信息
     * @return
     */
    List<SoftInfoVo> getAllUploadInfo();

    /**
     * 根据参数获取待签名文件路径
     * @param govUserId
     * @return
     */
    List<SignFileInfo> SignFileInfos(@Param("govUserId") Integer govUserId);






}
