package com.ustb.softverify.utils.checkcode;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class HmacUtils {

    public static final String KEY_MAC = "HmacSHA1";
    private static final String ENCODING = "UTF-8";
    //密钥
    private static final String key = "O483Ig8TPPxBf8Flx/47XBywjYovEXSG2KDy5NqM+10F7N9okO5zLf8jBcS/YIwlAqzMZpAMuJpJQ5ONfdqI0Q==";


    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {

        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    /**
     * 使用类中默认的key加密hash
     * @param dataStr 文件字符串
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(String dataStr) throws Exception {

        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(dataStr.getBytes(StandardCharsets.UTF_8));
    }

    private static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    private static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    public static void main(String[] args) throws Exception{
        String inputStr="这是一个测试字符串aaabbbccc1jkasjdlaksj11222333";
        byte[] inputData = inputStr.getBytes();
//        String key = initMacKey();
        System.out.println(new BigInteger(encryptHMAC(inputData, key)));
    }


}
