package com.ustb.softverify;

import com.ustb.softverify.service.ZipCompress;
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
        String zipFilePath = "/Users/linyi/typora/test.rar";
        String desDirectory = "/Users/linyi/typora/new";
        try {
            zipCompress.unzip(zipFilePath,desDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
