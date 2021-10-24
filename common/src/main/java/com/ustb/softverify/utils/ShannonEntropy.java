package edu.ustb.shannon;

import java.io.FileInputStream;
import java.util.*;

import static java.lang.Math.log;


public class ShannonEntropy {

    private static final int bufSize = 128;

    /**
     * 以8字节为缓冲区读取二进制文件，计算缓冲区内样本数量
     */
    public static Map<String, Integer> byteFile2String(String fileName) throws Exception {
        Map<String, Integer> numberMap = new HashMap<>();
        FileInputStream in = new FileInputStream(fileName);
        byte[] buffer = new byte[bufSize];
        int len;
        StringBuilder stringBuilder1 = new StringBuilder();
        String bitString;
        String bitSubString;
        int flag = 0;
        while ((len = in.read(buffer)) != -1) {

            if (len == bufSize) {
                for (byte b : buffer) {
                    flag++;
                    if (flag == bufSize) {
                        bitString = stringBuilder1.toString();
                        calNumberOfSimple(numberMap, bitString);
                        stringBuilder1 = stringBuilder1.delete(0, (bufSize - 2) * 8 + 1);
                        flag = 0;
                    } else {
                        stringBuilder1.append(byte2String(b));
                    }
                }
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                for (byte b : buffer) {
                    if (len == 0)
                        break;
                    stringBuilder2.append(byte2String(b));
                    len--;
                }
                bitString = stringBuilder2.toString();
                calNumberOfSimple(numberMap, bitString);
            }
        }
        return numberMap;
    }


    /**
     * 计算每类样本出现的次数
     * @param numberMap
     * @param bitString
     */
    public static void calNumberOfSimple(Map<String, Integer> numberMap, String bitString) {
        String substring;
        for (int i = 0; i < bitString.length() - 8; i++) {
            substring = bitString.substring(i, i + 8);
            if (numberMap.containsKey(substring)) {
                numberMap.put(substring, numberMap.get(substring) + 1);
            } else {
                numberMap.put(substring, 1);
            }
        }
    }

    /**
     *  将一个字节的二进制文件转化成字符串
     */
    public static String byte2String(byte b) {
        String s = "";
        for (int i = 0; i < 8; i++) {
            byte temp = (byte) ((byte) (b >> 7 - i) & 1);
            s += temp;
        }
        return s;
    }


    // 计算概率和香农熵
    public static byte[] calculateShanonEntropy(String fileName) {

        double px, sum = 0, shanon = 0;
        try {
            Map<String, Integer> numberMap = byteFile2String(fileName);
            // 统计单个样本个数
            int number;
            // 统计样本总个数
            int numberOfSample = 0;
            for (String s : numberMap.keySet()) {
                number = numberMap.get(s);
                // System.out.println(integer);
                numberOfSample += number;
            }
            for (String s : numberMap.keySet()) {
                number = numberMap.get(s);
                px = (double) number / numberOfSample;
                // System.out.print(px + " ");
                sum += (px * (log(px) / log(2)));
            }
            shanon = -sum;
            // 归一
            shanon = shanon / 8;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // double2Bytes(shanon);
        System.out.println(shanon);

        int res28 = (int) (shanon  * 1024 * 1024 * 256 );
        int res16 = (int) (shanon * 64 * 1024);
        String binaryString16 = Integer.toBinaryString(res16);
        String binaryString28 = Integer.toBinaryString(res28);

//        byte[] bytes = double2Bytes(shanon);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (byte aByte : bytes) {
//           stringBuilder.append(byte2String(aByte));
//        }
//        String s = stringBuilder.toString();
//        System.out.println("shannon " + s);
        System.out.println("binaryString16 " +binaryString16);
        System.out.println("binaryString28 " +binaryString28);
//        System.out.println("res28 " + res28);
//        System.out.println("res16 " + res16);

        String substring1 = binaryString16.substring(0, 8);
        String substring2 = binaryString16.substring(9, 16);

        byte b1 = bit2byte(substring1);
        byte b2 = bit2byte(substring2);

        byte[] res = new byte[]{b1,b2};
        return  res;
    }

    /**
     *  将double转化成byte[4]数组
     * @param d
     * @return
     */
    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
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

}

