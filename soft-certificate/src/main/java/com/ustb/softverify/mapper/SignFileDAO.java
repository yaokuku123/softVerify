package com.ustb.softverify.mapper;

import com.ustb.softverify.entity.dto.IdentityInfo;
import com.ustb.softverify.entity.dto.SignIdentityInfo;
import com.ustb.softverify.entity.po.SignFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-11 10:10
 */
public interface SignFileDAO {

    void insert(SignFile signFile);

    /**
     * 获取需要签名文件的列表
     * @param identityInfo 用户标识和软件名称
     * @return 文件列表
     */
    List<SignFile> listSignFilePath(IdentityInfo identityInfo);

    /**
     * 更新签名文件的交易id
     * @param fid 被签名文件的交易id
     */
    void updateTxidById(@Param("fid") Integer fid,@Param("txid") String txid);
}
