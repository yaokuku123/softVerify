package com.ustb.softverify.utils.checkcode;

import com.ustb.softverify.utils.MD5Utils;

public class TestDemo {
    public static void main(String[] args) throws Exception {

//        String filePath1 = "/Users/yorick/Documents/work/project/softVerify/TempSoftwareLibrary/2021/20111/origin/version.bat";
//        String filePath2 = "/Users/yorick/Documents/work/project/softVerify/TempSoftwareLibrary/2021/20111/origin/CoreFiles.txt";
//        String filePath3 = "/Users/yorick/Documents/work/project/softVerify/TempSoftwareLibrary/2021/20111/origin/version.bat";
//        String txid = "8bcd10cca9397fed79c5566b5921a531ea9c27d3a5031cb948ea7e23db0817b7";
//        String fingerCode = CheckCode.getFingerCode(filePath1, filePath2, filePath3, txid);
//        System.out.println(fingerCode);

        int i = Integer.parseInt("851029")  ^ 1010;
        System.out.println(i);



//        byte[] file1 = HmacUtils.encryptHMAC(FileUtil.read(filePath1));
//        byte[] fixBytesFile1 = ByteAndBitUtils.getFixBytes(file1, 4);
//        byte[] file2 = HmacUtils.encryptHMAC(FileUtil.read(filePath2));
//        byte[] fixBytesFile2 = ByteAndBitUtils.getFixBytes(file2, 2);
//        byte[] file3 = HmacUtils.encryptHMAC(FileUtil.read(filePath3));
//        byte[] fixBytesFile3 = ByteAndBitUtils.getFixBytes(file3, 2);
//        byte[] txid = HexUtils.hex2Bytes("8bcd10cca9397fed79c5566b5921a531ea9c27d3a5031cb948ea7e23db0817b7");
//        byte[] fixBytesTxid = ByteAndBitUtils.getFixBytes(txid, 3);
//
//        //文件个数
//        String fileNum = "00001100";
//        byte fileNumByte = ByteAndBitUtils.bit2byte(fileNum);
//        //txid版本
//        String txidVersion = "00000001";
//        byte txidVersionByte = ByteAndBitUtils.bit2byte(txidVersion);
//        byte fileNumAndTxidVersionByte = (byte) (fileNumByte | txidVersionByte);
//
//        //获取sm3 hash校验码
//        byte[] unValid = CheckCode.getCheckCode3(fileNumAndTxidVersionByte,fixBytesFile1, fixBytesFile2, fixBytesFile3, new byte[2], new byte[2], fixBytesTxid);
//        byte[] sm3EncryptByte = SM3Algorithm.SM3Encrypt(unValid);
//        byte fixBytes = (byte) (ByteAndBitUtils.getFixBytes(sm3EncryptByte, 1)[0] & ByteAndBitUtils.bit2byte("11110000"));
//        //拼接第一个字节
//        byte valid = (byte)(fixBytes | fileNumAndTxidVersionByte);
//        //结果
//        byte[] res = CheckCode.getCheckCode3(valid, fixBytesFile1, fixBytesFile2, fixBytesFile3, new byte[2], new byte[2], fixBytesTxid);
//        System.out.println(HexUtils.bytes2Hex(res));

    }
}
