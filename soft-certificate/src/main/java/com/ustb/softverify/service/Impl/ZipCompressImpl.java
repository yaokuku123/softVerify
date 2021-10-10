package com.ustb.softverify.service.Impl;

import com.ustb.softverify.service.ZipCompress;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ZipCompressImpl implements ZipCompress {

    @Override
    public String unzip(String zipFilePath, String desDirectory) throws Exception{
        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }
        // 读入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        // 遍历每一个文件
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        String unzipName = zipEntry.getName();

        while (zipEntry != null) {
            if (zipEntry.isDirectory()) { // 文件夹
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                // 直接创建
                mkdir(new File(unzipFilePath));
            } else { // 文件
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                File file = new File(unzipFilePath);
                // 创建父目录
                mkdir(file.getParentFile());
                // 写出文件流
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(new FileOutputStream(unzipFilePath));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        unzipName= unzipName.substring(0, unzipName.length()-1);
        return unzipName;
    }

    private void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();

    }

    @Override
    public void changeroot(String zipFilePath) {
        try {
            Runtime.getRuntime().exec("chmod 444" +zipFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
