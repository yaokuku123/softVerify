package com.ustb.softverify;

import com.ustb.softverify.utils.checkcode.ShannonEntropy;
import org.junit.Test;

public class ShannonTest {

    @Test
    public void testShannon10M(){
        long startTime = System.currentTimeMillis();
        String fileName = "D:\\Projects\\JavaProjects\\202\\Shannon-Entropy\\file\\VMware-workstation-full-15.5.6-16341506.exe";
        byte[] bytes = ShannonEntropy.calculateShanonEntropy(fileName);
        for (byte aByte : bytes) {
            System.out.print(ShannonEntropy.byte2String(aByte));
        }
        long endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println(endTime-startTime);
    }
}
