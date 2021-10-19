package com.ustb.softverify;

import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.utils.ReadTxt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReadTxtTest {
    @Test
    public void testComp2txt() {
        ReadTxt readTxt = new ReadTxt();

        String filePath = "D:\\WORK\\TargetField\\CoreFiles.txt";
        String s1 = "java_error_in_webstorm64.hprof";
        String s2 = "1,155,720,249";
        String s3 = "java_error_in_webstorm64.hprof1";
        String s4 = "1,155,720,249";
        boolean status = false;

        List<CompInfo> compInfoList = new ArrayList<CompInfo>();
        CompInfo compInfo1 = new CompInfo(s1, s2);
        CompInfo compInfo2 = new CompInfo(s3, s4);
        CompInfo compInfo3 = new CompInfo(s1, s2);

        compInfoList.add(compInfo1);
        compInfoList.add(compInfo2);
        compInfoList.add(compInfo3);

        status = readTxt.comp2txt(filePath, compInfoList);

        System.out.println(status);
    }
}
