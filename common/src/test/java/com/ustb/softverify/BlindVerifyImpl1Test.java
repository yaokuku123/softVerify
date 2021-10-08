package com.ustb.softverify;

import com.ustb.softverify.algorithm.BlindAlgorithm;
import com.ustb.softverify.algorithm.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.QueryParam;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class BlindVerifyImpl1Test {

    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";

    @Test
    public void algorithmTest() {
        String filePath = "/Users/yorick/Downloads/auth-resource.jar";
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(filePath);
        //初始化
        Map<String, Object> keyMap = algorithm.initParams();
        //签名
        PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(filePath, publicKey , (Element) keyMap.get(PRIVATE_KEY));
        //查询
        QueryParam queryParam = algorithm.check(filePath, publicKey.getPairing(), signList);
        //验证
        Boolean res = algorithm.verify(publicKey, queryParam);
        System.out.println(res);
    }
}
