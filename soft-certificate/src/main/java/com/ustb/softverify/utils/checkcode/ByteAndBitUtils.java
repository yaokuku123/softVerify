package com.ustb.softverify.utils.checkcode;

public class ByteAndBitUtils {
    /**
     * 将字节转换为位的字符串形式
     * @param b 字节
     * @return 字节的字符串表示
     */
    public static String byte2String(byte b) {
        String s = "";
        for (int i = 0; i < 8; i++) {
            byte temp = (byte) ((byte) (b >> 7 - i) & 1);
            s += temp;
        }
        return s;
    }

    /**
     * 将二进制字符串转换回字节
     * @param bString 二进制字符串
     * @return 对应的字节
     */
    public static byte bit2byte(String bString){
        byte result=0;
        for(int i=bString.length()-1,j=0;i>=0;i--,j++){
            result+=(Byte.parseByte(bString.charAt(i)+"")*Math.pow(2, j));
        }
        return result;
    }



    /**
     * 获取bytes数组的前指定位数的字节
     * @param bytes 原字节数组
     * @param number 指定的数组位数
     * @return 指定位数的数组
     */
    public static byte[] getFixBytes(byte[] bytes,int number) {
        byte[] res = new byte[number];
        for (int i = 0; i < number; i++) {
            res[i] = bytes[i];
        }
        return res;
    }
}
