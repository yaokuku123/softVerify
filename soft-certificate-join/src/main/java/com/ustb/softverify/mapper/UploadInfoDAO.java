package com.ustb.softverify.mapper;

import org.apache.ibatis.annotations.Param;

public interface UploadInfoDAO {

    /**
     * 添加用户上传的交易记录地址信息
     * @param govUserId
     * @param txid
     */
    void insertTxid(@Param("govUserId") Integer govUserId, @Param("txid") String txid);
}
