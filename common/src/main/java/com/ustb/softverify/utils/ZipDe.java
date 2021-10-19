package com.ustb.softverify.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;

public class ZipDe {
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
}
