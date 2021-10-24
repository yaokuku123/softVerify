package com.ustb.softverify.utils.checkcode;

import java.nio.ByteBuffer;

public class CheckCode {

    /**
     * 拼接核验码，读取三个文件的byte拼接
     * @param file1 文件1 4B
     * @param file2 文件2 2B
     * @param file3 文件3 2B
     * @param entropy 信息熵 2B
     * @param undefine 待定 2B
     * @param txid 交易地址 3B
     * @param valid 文件个数，版本号，奇偶校验 1B
     * @return 核验码
     */
    public static byte[] getCheckCode3(byte valid,byte[] file1,byte[] file2,byte[] file3,byte[] entropy,
                                       byte[] undefine,byte[] txid) {
        return ByteBuffer.allocate(16)
                .put(valid)
                .put(file1)
                .put(file2)
                .put(file3)
                .put(entropy)
                .put(undefine)
                .put(txid)
                .array();
    }

    /**
     * 拼接核验码，读取三个文件的byte拼接
     * @param file1 文件1 4B
     * @param file2 文件2 4B
     * @param entropy 信息熵 2B
     * @param undefine 待定 2B
     * @param txid 交易地址 3B
     * @param valid 文件个数，版本号，奇偶校验 1B
     * @return 核验码
     */
    public static byte[] getCheckCode2(byte valid,byte[] file1,byte[] file2,byte[] entropy,
                                      byte[] undefine,byte[] txid) {
        return ByteBuffer.allocate(16)
                .put(valid)
                .put(file1)
                .put(file2)
                .put(entropy)
                .put(undefine)
                .put(txid)
                .array();
    }

    /**
     * 拼接核验码，读取三个文件的byte拼接
     * @param file1 文件1 8B
     * @param entropy 信息熵 2B
     * @param undefine 待定 2B
     * @param txid 交易地址 3B
     * @param valid 文件个数，版本号，奇偶校验 1B
     * @return 核验码
     */
    public static byte[] getCheckCode1(byte valid,byte[] file1,byte[] entropy,
                                       byte[] undefine,byte[] txid) {
        return ByteBuffer.allocate(16)
                .put(valid)
                .put(file1)
                .put(entropy)
                .put(undefine)
                .put(txid)
                .array();
    }


}
