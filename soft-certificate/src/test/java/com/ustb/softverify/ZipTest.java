package com.ustb.softverify;

import com.ustb.softverify.service.ZipCompress;
import com.ustb.softverify.utils.ZipUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ZipTest {

    @Autowired
    private ZipCompress zipCompress;

    @Test
    public void test(){
        String zipFilePath = "/Users/yorick/Downloads/hi.zip";
        String desDirectory = "/Users/yorick/Downloads/";
        ZipUtil.decompressZip(zipFilePath,desDirectory);
    }
}
