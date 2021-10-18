package com.ustb.softverify.utils;

import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//import net.lingala.zip4j.core.ZipFile;
//import net.lingala.zip4j.model.FileHeader;


public class ZipUtil {

    /**
     * 解压
     * @param zipPath
     * @param descDir
     * @return
     */
    public static boolean decompressZip(String zipPath, String descDir) {

        File zipFile = new File(zipPath);
        boolean flag = false;
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        try {
            System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding"));
//          zip = new ZipFile(zipFile, Charset.forName("UTF-8"));//防止中文目录，乱码
            String encoding = getEncoding(zipPath);
            zip = new ZipFile(zipFile, Charset.forName(encoding));//防止中文目录，乱码
            String name = zip.getName();
            System.out.println(name);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                //指定解压后的文件夹+当前zip文件的名称
                String outPath = (descDir + "/" + zipEntryName).replace("/", File.separator);
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            flag = true;
            //必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
            zip.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

//    public static void decompressZip(String source, String target, String targetName) throws Exception {
//        try {
//            File file = new File(source);
//            if (!file.exists() || file.isDirectory()) {
//                throw new Exception("将要解压文件不存在或路径不正确！");
//            }
//
//            file = new File(target + File.separator + targetName);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            ZipFile zipfile = new ZipFile(source);
//            if (!zipfile.isValidZipFile()) {
//                throw new Exception("压缩文件不合法,可能被损坏.");
//            }
//            zipfile.setFileNameCharset("UTF-8");
//            zipfile.setFileNameCharset("GBK");
//            zipfile.extractAll(target + File.separator + targetName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//
//    }

//    public static void unZip(String zipPath, String destDir) throws Exception {
//        ZipFile zipFile = new ZipFile(zipPath);
//        zipFile.setFileNameCharset(getEncoding(zipPath));
//        zipFile.extractAll(destDir);
//    }

    @SuppressWarnings("unchecked")
    private static String getEncoding(String path) throws Exception {
        String encoding = "GBK";
        net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(path);
        zipFile.setFileNameCharset(encoding);
        List<FileHeader> list = zipFile.getFileHeaders();
        for (int i = 0; i < list.size(); i++) {
            FileHeader fileHeader = list.get(i);
            String fileName = fileHeader.getFileName();
            if (isMessyCode(fileName)) {
                encoding = "UTF-8";
                break;
            }
        }
        return encoding;
    }

    private static boolean isMessyCode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            // 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            if ((int) c == 0xfffd) {
                // 存在乱码
                return true;
            }
        }
        return false;
    }

}
