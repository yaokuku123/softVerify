package com.ustb.softverify;

import com.ustb.softverify.utils.ZipDe;
import org.junit.Test;

public class ZipTest {

    @Test
    public void testZipDe() {
        ZipDe zipDe = new ZipDe();
        zipDe.unzipWithPassword("D:\\WORK\\TargetField\\zipTest3.zip", "D:\\WORK\\TargetField\\unZip", "123456");
    }
}
