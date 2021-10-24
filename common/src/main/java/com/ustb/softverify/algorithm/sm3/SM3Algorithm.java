package com.ustb.softverify.algorithm.sm3;

import com.ustb.softverify.utils.HashBasicOperaterSetUtil;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class SM3Algorithm {

    /**
     *
     * @param bytes 需要校验部分
     * @return 哈希结果
     */
    public static byte[] SM3Encrypt(byte[] bytes) {
        SM3Digest digest = new SM3Digest();
        digest.update(bytes,0,bytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash,0);
        return hash;
    }

    /**
     *
     * @param filePath 文件路径
     * @return 哈希结果
     * @throws FileNotFoundException
     */
    public static byte[] SM3Encrypt(String filePath) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        String resultHexString = "";
        byte[] sm3Bytes = null;
        try {
            SM3Digest sm3Digest = new SM3Digest();
            byte[] buffer = new byte[1024];
            int length = 1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                sm3Digest.update(buffer, 0, length);
            }
            fis.close();
            sm3Bytes = new byte[sm3Digest.getDigestSize()];
            sm3Digest.doFinal(sm3Bytes, 0);
            return sm3Bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sm3Bytes;
    }

    /**
     * 采用密钥加密
     * @param key 密钥
     * @param filePath 文件路径
     * @return 哈希结果
     */
    public static byte[] hmac(byte[] key, String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        byte[] buffer = new byte[1024];
        int length = 1;
        while ((length = fis.read(buffer, 0, 1024)) != -1) {
            mac.update(buffer, 0, length);
        }
        fis.close();
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    /**
     * 验证
     * @param filePath 验证文件所在路径
     * @param sm3HexString 原文件哈希值
     * @return 验证结果
     */
    public static boolean verify(String filePath, String sm3HexString) {
        boolean flag = false;
        try {
            byte[] sm3Hash = HashBasicOperaterSetUtil.hexToByte(sm3HexString);
            byte[] newHash = SM3Encrypt(filePath);
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static boolean verify(String filePath, String sm3HexString, byte[] key) {
        boolean flag = false;
        try {
            byte[] sm3Hash = HashBasicOperaterSetUtil.hexToByte(sm3HexString);
            byte[] newHash = hmac(key, filePath);
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
