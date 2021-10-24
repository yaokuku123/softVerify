package com.ustb.softverify.utils.checkcode;

import com.ustb.softverify.algorithm.sm3.SM3Algorithm;
import com.ustb.softverify.utils.FileUtil;

import java.nio.ByteBuffer;

public class CheckCode {


    /**
     * 三个文件
     * @param filePath1 文件1
     * @param filePath2 文件2
     * @param filePath3 文件3
     * @param txidStr 交易地址 16进制字符串
     * @return 核验码
     */
    public static String getFingerCode(String filePath1,String filePath2,String filePath3,String txidStr) {
        try {
            //第一个文件转换
            byte[] file1 = HmacUtils.encryptHMAC(FileUtil.read(filePath1));
            byte[] fixBytesFile1 = ByteAndBitUtils.getFixBytes(file1, 4);
            //第二个文件转换
            byte[] file2 = HmacUtils.encryptHMAC(FileUtil.read(filePath2));
            byte[] fixBytesFile2 = ByteAndBitUtils.getFixBytes(file2, 2);
            //第三个文件转换
            byte[] file3 = HmacUtils.encryptHMAC(FileUtil.read(filePath3));
            byte[] fixBytesFile3 = ByteAndBitUtils.getFixBytes(file3, 2);
            //txid交易地址转换
            byte[] txid = HexUtils.hex2Bytes(txidStr);
            byte[] fixBytesTxid = ByteAndBitUtils.getFixBytes(txid, 3);

            //文件3个 11
            String fileNum = "00001100";
            byte fileNumByte = ByteAndBitUtils.bit2byte(fileNum);
            //txid版本 01
            String txidVersion = "00000001";
            byte txidVersionByte = ByteAndBitUtils.bit2byte(txidVersion);
            //拼接
            byte fileNumAndTxidVersionByte = (byte) (fileNumByte | txidVersionByte);

            //获取sm3 hash校验码 4位
            byte[] unValid = CheckCode.getCheckCode3(fileNumAndTxidVersionByte,fixBytesFile1, fixBytesFile2, fixBytesFile3, new byte[2], new byte[2], fixBytesTxid);
            byte[] sm3EncryptByte = SM3Algorithm.SM3Encrypt(unValid);
            byte fixBytes = (byte) (ByteAndBitUtils.getFixBytes(sm3EncryptByte, 1)[0] & ByteAndBitUtils.bit2byte("11110000"));
            //拼接第一个字节
            byte valid = (byte)(fixBytes | fileNumAndTxidVersionByte);
            //结果
            byte[] res = CheckCode.getCheckCode3(valid, fixBytesFile1, fixBytesFile2, fixBytesFile3, new byte[2], new byte[2], fixBytesTxid);
            System.out.println(HexUtils.bytes2Hex(res));
            return HexUtils.bytes2Hex(res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 两个文件
     * @param filePath1 文件1
     * @param filePath2 文件2
     * @param txidStr 交易地址 16进制字符串
     * @return 核验码
     */
    public static String getFingerCode(String filePath1,String filePath2,String txidStr) {
        try {
            //第一个文件转换
            byte[] file1 = HmacUtils.encryptHMAC(FileUtil.read(filePath1));
            byte[] fixBytesFile1 = ByteAndBitUtils.getFixBytes(file1, 4);
            //第二个文件转换
            byte[] file2 = HmacUtils.encryptHMAC(FileUtil.read(filePath2));
            byte[] fixBytesFile2 = ByteAndBitUtils.getFixBytes(file2, 4);
            //txid交易地址转换
            byte[] txid = HexUtils.hex2Bytes(txidStr);
            byte[] fixBytesTxid = ByteAndBitUtils.getFixBytes(txid, 3);

            //文件2个 10
            String fileNum = "00001000";
            byte fileNumByte = ByteAndBitUtils.bit2byte(fileNum);
            //txid版本 01
            String txidVersion = "00000001";
            byte txidVersionByte = ByteAndBitUtils.bit2byte(txidVersion);
            //拼接
            byte fileNumAndTxidVersionByte = (byte) (fileNumByte | txidVersionByte);

            //获取sm3 hash校验码 4位
            byte[] unValid = CheckCode.getCheckCode2(fileNumAndTxidVersionByte,fixBytesFile1, fixBytesFile2, new byte[2], new byte[2], fixBytesTxid);
            byte[] sm3EncryptByte = SM3Algorithm.SM3Encrypt(unValid);
            byte fixBytes = (byte) (ByteAndBitUtils.getFixBytes(sm3EncryptByte, 1)[0] & ByteAndBitUtils.bit2byte("11110000"));
            //拼接第一个字节
            byte valid = (byte)(fixBytes | fileNumAndTxidVersionByte);
            //结果
            byte[] res = CheckCode.getCheckCode2(valid, fixBytesFile1, fixBytesFile2, new byte[2], new byte[2], fixBytesTxid);
            System.out.println(HexUtils.bytes2Hex(res));
            return HexUtils.bytes2Hex(res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个文件
     * @param filePath1 文件1
     * @param txidStr 交易地址 16进制字符串
     * @return 核验码
     */
    public static String getFingerCode(String filePath1,String txidStr) {
        try {
            //第一个文件转换
            byte[] file1 = HmacUtils.encryptHMAC(FileUtil.read(filePath1));
            byte[] fixBytesFile1 = ByteAndBitUtils.getFixBytes(file1, 8);
            //txid交易地址转换
            byte[] txid = HexUtils.hex2Bytes(txidStr);
            byte[] fixBytesTxid = ByteAndBitUtils.getFixBytes(txid, 3);

            //文件1个 01
            String fileNum = "00000100";
            byte fileNumByte = ByteAndBitUtils.bit2byte(fileNum);
            //txid版本 01
            String txidVersion = "00000001";
            byte txidVersionByte = ByteAndBitUtils.bit2byte(txidVersion);
            //拼接
            byte fileNumAndTxidVersionByte = (byte) (fileNumByte | txidVersionByte);

            //获取sm3 hash校验码 4位
            byte[] unValid = CheckCode.getCheckCode1(fileNumAndTxidVersionByte,fixBytesFile1, new byte[2], new byte[2], fixBytesTxid);
            byte[] sm3EncryptByte = SM3Algorithm.SM3Encrypt(unValid);
            byte fixBytes = (byte) (ByteAndBitUtils.getFixBytes(sm3EncryptByte, 1)[0] & ByteAndBitUtils.bit2byte("11110000"));
            //拼接第一个字节
            byte valid = (byte)(fixBytes | fileNumAndTxidVersionByte);
            //结果
            byte[] res = CheckCode.getCheckCode1(valid, fixBytesFile1, new byte[2], new byte[2], fixBytesTxid);
            System.out.println(HexUtils.bytes2Hex(res));
            return HexUtils.bytes2Hex(res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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
     private static byte[] getCheckCode3(byte valid,byte[] file1,byte[] file2,byte[] file3,byte[] entropy,
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
    private static byte[] getCheckCode2(byte valid,byte[] file1,byte[] file2,byte[] entropy,
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
    private static byte[] getCheckCode1(byte valid,byte[] file1,byte[] entropy,
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
