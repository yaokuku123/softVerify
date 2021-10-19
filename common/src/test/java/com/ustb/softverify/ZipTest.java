package com.ustb.softverify;

import com.ustb.softverify.utils.ZipDe;
import net.lingala.zip4j.exception.ZipException;
import org.junit.Test;

public class ZipTest {

    @Test
    public void testZipDe() throws ZipException {
        //zipDe.unzipWithPassword("D:\\WORK\\TargetField\\zipTest3.zip", "D:\\WORK\\TargetField\\unZip", "123456");
        ZipDe.zip("D:\\Projects\\softVerify\\data\\tmp","D:\\Projects\\softVerify\\data\\a.zip");
    }
}
