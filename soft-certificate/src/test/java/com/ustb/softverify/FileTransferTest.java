package com.ustb.softverify;

import com.ustb.softverify.utils.EnvUtils;
import com.ustb.softverify.utils.FileUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author WYP
 * @date 2021-10-20 0:15
 */
public class FileTransferTest {


    @Test
    public void test() {
        System.out.println(EnvUtils.ROOT_PATH + "a.txt");

            FileUtil.copyFile("D:\\file\\a.txt", EnvUtils.ROOT_PATH);


    }


}
