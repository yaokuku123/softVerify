package com.ustb.softverify;

import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.utils.ReadTxt;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TextTest {

    @Test
    public void test() throws UnsupportedEncodingException {
        String dirPath = "/Users/yorick/Downloads/CoreFiles.txt";
        List<CompInfo> compInfos = new ArrayList<>();
        CompInfo compInfo1 = new CompInfo();
        compInfo1.setOrgName("version.bat");
        compInfo1.setFileSize(2026l);
        CompInfo compInfo2 = new CompInfo();
        compInfo2.setOrgName("version.sh");
        compInfo2.setFileSize(1908l);
        compInfos.add(compInfo1);
        compInfos.add(compInfo2);
        System.out.println(ReadTxt.comp2txt(dirPath, compInfos));
    }

    @Test
    public void test1() {
        String a = "2021/10/19  22:26                1,2 BAKA.txt";
        String Str = new String("Runoob");
        String spString[] = a.split("\\s+");
        for(String ss : spString){
            System.out.println(ss);
        }


        spString[2] = spString[2].replace(",", "");
        System.out.println(spString[2]);

        System.out.println(Str.replace('o', 'T'));
    }


}
