package com.ustb.softverify;

import com.ustb.softverify.algorithm.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.utils.HashBasicOperaterSetUtil;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * @author WYP
 * @date 2021-10-10 21:25
 */
public class SM3Test {

    @Test
    public void test() throws FileNotFoundException {

        //FIXME
        String softDestPath = "D:\\typora-scrolls-0.5.zip";
        BlindVerifyAlgorithmImpl1 bva = new BlindVerifyAlgorithmImpl1(softDestPath);
        String hash = HashBasicOperaterSetUtil.byteToHex(bva.SM3Encrypt(softDestPath));
        System.out.println(hash);
        boolean verify = bva.verify(softDestPath, hash);
        System.out.println(verify);
    }
}
