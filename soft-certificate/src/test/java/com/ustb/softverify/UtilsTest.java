package com.ustb.softverify;

import ch.ethz.ssh2.Connection;
import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.service.Impl.ShellTools;
import com.ustb.softverify.utils.*;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class UtilsTest {

    @Autowired
    private ShellTools shellTools;

    @Test
    public void testZipDe() {
        ZipDe zipDe = new ZipDe();
        zipDe.unzipWithPassword("D:\\WORK\\TargetField\\test\\解压.zip", "D:\\WORK\\TargetField\\test\\UnZip", "123456");
    }

    @Test
    public void testComp2txt() throws UnsupportedEncodingException {
        ReadTxt readTxt = new ReadTxt();

        String filePath = "D:\\WORK\\TargetField\\CoreFiles.txt";
        String s1 = "java_error_in_webstorm64.hprof";
        String s2 = "1,155,720,249";
        String s3 = "java_error_in_webstorm64.hprof";
        String s4 = "1,155,720,249";
        boolean status = false;

        List<CompInfo> compInfoList = new ArrayList<CompInfo>();
        CompInfo compInfo1 = new CompInfo(s1, Long.valueOf(s1));
        CompInfo compInfo2 = new CompInfo(s3, Long.valueOf(s4));
        CompInfo compInfo3 = new CompInfo(s1, Long.valueOf(s2));

        compInfoList.add(compInfo1);
        compInfoList.add(compInfo2);
        compInfoList.add(compInfo3);

        status = readTxt.comp2txt(filePath, compInfoList);

        System.out.println(status);
    }

    @Test
    public void scpTest() {
        ScpUtil scpUtil = new ScpUtil();

        try {
            //scpUtil.getFile("/root/scpTest/BAKA.txt", "D:\\WORK\\TargetField");
            scpUtil.putFile("D:\\file\\a.txt", "/root/test/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void zipTest() throws ZipException {
        System.out.println(EnvUtils.ROOT_PATH);
        ZipDe.zip(EnvUtils.ROOT_PATH,EnvUtils.ROOT_PATH+"/a.zip");
    }


    @Test
    public void zipTest1(){

        FileUtil.deleteDir("D:\\file");
    }

    @Test
    public void scpTest1() throws IOException {
        String path = "D:\\file";
        Connection conn = shellTools.getConn("123.56.246.148", "root", "chainNode202");
        shellTools.scpRemoteFile(conn,"/root/test/",path);
    }

    @Test
    public void testWrite() throws Exception {
        FileOutputStream fos = new FileOutputStream("D:\\abc.csv");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("姓名", "年龄", "家乡");
        CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);

//        csvPrinter = CSVFormat.DEFAULT.withHeader("姓名", "年龄", "家乡").print(osw);

        for (int i = 0; i < 10; i++) {
            csvPrinter.printRecord("张三", 20, "湖北");
        }

        csvPrinter.flush();
        csvPrinter.close();
    }


}
