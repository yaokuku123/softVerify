package com.ustb.softverify.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class ZipDe {
    public static void main(String[] args) {
        zipFile("D:\\file\\file.zip","D:\\file\\a.zip","123456");


    }
    public void unzipWithPassword(String zipPath, String destPath, String password){
        // 先判断zip源文件是否存在，不存在退出
        if (StringUtils.isBlank(zipPath)){
            return;
        }
        ZipFile zipFile;
        try{
            zipFile = new ZipFile(zipPath);
            // 设置字符集
            zipFile.setFileNameCharset("utf-8");
            // 判断是否加密
            if (zipFile.isEncrypted()){
                // 添加密码
                zipFile.setPassword(password);
            }
            //解压
            zipFile.extractAll(destPath);
        }catch (Exception e){
            System.out.println("解压失败");
        }
    }

    public static void zip(String FilePath,String zipFilePath) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);

            String folderToAdd = FilePath;

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            zipFile.addFolder(folderToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 压缩指定路径的文件
     * @param srcFilePath 待压缩文件路径
     * @param zipPathFileName zip文件全路径名
     * @param password 加密密码
     * @return
     */
    public static boolean zipFile(String srcFilePath, String zipPathFileName, String password){

        try {
            // 生成的压缩文件
            ZipFile zipFile = new ZipFile(zipPathFileName);
            ZipParameters parameters = new ZipParameters();
            // 压缩级别
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if(!StringUtils.isEmpty(password)){
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                parameters.setPassword(password);
            }

            // 要打包的文件夹
            File currentFile = new File(srcFilePath);
            File[] fs = currentFile.listFiles();
            // 遍历test文件夹下所有的文件、文件夹
            for (File f : fs) {
                if (f.isDirectory()) {
                    zipFile.addFolder(f.getPath(), parameters);
                } else {
                    zipFile.addFile(f, parameters);
                }
            }
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     *  @param zipFileFullName zip文件所在的路径名
     * @param filePath 解压到的路径
     * @param password 需要解压的密码
     * @return
     */
    public static boolean unZipFile(String zipFileFullName, String filePath, String password) {
        try {
            ZipFile zipFile = new ZipFile(zipFileFullName);
            // 如果解压需要密码
            if(StringUtils.isNotEmpty(password)&&zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(filePath);
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
    }
}
