package com.ustb.softverify;

import com.ustb.softverify.entity.dto.CompInfo;
import com.ustb.softverify.utils.ReadTxt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CompareContentTest {

    @Test
    public void test() {
        String dirPath = "/Users/yorick/Downloads/CoreFiles.txt";
        List<CompInfo> compInfos = new ArrayList<>();
        CompInfo compInfo1 = new CompInfo();
        compInfo1.setOrgName("version.sh");
        compInfo1.setFileSize("1908");
        compInfos.add(compInfo1);
        CompInfo compInfo2 = new CompInfo();
        compInfo2.setOrgName("version.bat");
        compInfo2.setFileSize("2026");
        compInfos.add(compInfo2);

        System.out.println(ReadTxt.comp2txt(dirPath, compInfos));

    }
}
