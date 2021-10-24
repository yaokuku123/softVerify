package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import io.swagger.models.auth.In;
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
     * @param pid
     * @return
     */
    List<SignFileInfo> SignFileInfos(@Param("pid") String pid);

    /**
     * 根据用户标识获取软件列表
     * @param govUserId 用户标识
     * @return 软件列表信息
     */
    List<SoftInfo> listSoft(@Param("govUserId") Integer govUserId);

    /**
     * 插入软件信息
     * @param softInfo
     */
    Integer insertSoft(SoftInfo softInfo);

    /**
     * 根据用户标识获取
     * @param pid
     * @return
     */
    String findSoftName(@Param("pid") String pid);

    /**
     * 归档文件改变status
     * @param pid
     * @return
     */
    void changeStatus(@Param("pid") String pid);

    /**
     * 根据用户标识获取
     * @param govUserId
     * @return
     */
    Integer findSoftId(@Param("govUserId") Integer govUserId);


    void insertSignFile(@Param("fileName") String fileName,@Param("txid")String txid,@Param("sid")Integer sid);


    List<SignFile> getTxid(@Param("sid") Integer sid);

    SoftInfo getSoftInfo(@Param("pid") String pid);

    /**
     * 更新软件信息
     * @param softInfo
     */
    void updateSoft(SoftInfo softInfo);

    /**
     * 更新状态
     * @param govUserId
     * @param status
     */
    void updateStatus(@Param("govUserId") Integer govUserId,@Param("status") Integer status);


    void insertPath(@Param("pid")String pid,@Param("path")String path,@Param("zipName")String zipName);

    /**
     * 根据govUserId和状态信息查询软件
     * @param govUserId
     * @param status
     * @return
     */
    SoftInfo getSoftInfoByGovUserId(@Param("govUserId") Integer govUserId,@Param("status") Integer status);

    SoftInfo getSoftDetail(@Param("pid") String pid);

    void insertTxid(@Param("pid") String pid, @Param("txid") String txid);

    List<SignFileInfo> softFileRecords(@Param("pid") String pid);

    Integer getSid(@Param("pid")String pid);

    void insertZipPwd(@Param("pid")String pid,@Param("zipPassword") String zipPassword);

    /**
     * 更新软件状态信息为已提交状态
     * @param pid
     */
    void updateStatusToSubmit(String pid);
}
