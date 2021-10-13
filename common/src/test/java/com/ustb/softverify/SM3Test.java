package com.ustb.softverify;

import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.algorithm.sm3.SM3Algorithm;
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

        String softDestPath = "/Users/yorick/Downloads/3.zip";
        String hash = HashBasicOperaterSetUtil.byteToHex(SM3Algorithm.SM3Encrypt(softDestPath));
        System.out.println(hash);
        boolean verify = SM3Algorithm.verify(softDestPath, hash);
        System.out.println(verify);
    }
}
