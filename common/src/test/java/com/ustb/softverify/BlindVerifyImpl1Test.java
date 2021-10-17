package com.ustb.softverify;

import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.QueryParam;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlindVerifyImpl1Test {

    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";

    @Test
    public void algorithmTest() {
        String filePath = "C:\\Users\\WYP\\Desktop\\251.rmvb";
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(filePath);
        List<Long> list = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            //初始化
            Map<String, Object> keyMap = algorithm.initParams();
            //签名
            long start = System.currentTimeMillis();
            PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
            ArrayList<Element> signList = algorithm.sign(filePath, publicKey , (Element) keyMap.get(PRIVATE_KEY));
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            list.add(end - start);
            //查询
            QueryParam queryParam = algorithm.check(filePath, publicKey.getTypeAParams(), signList);
            //验证
            Boolean res = algorithm.verify(publicKey, queryParam);
            System.out.println(res);
        }
        System.out.println(list.stream().collect(Collectors.summarizingLong(e -> {
            return e;
        })));
    }

    @Test
    public void algorithmFailTest() {
        String filePath = "/Users/yorick/Downloads/auth-resource.jar";
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(filePath);
        //初始化
        Map<String, Object> keyMap = algorithm.initParams();
        //签名
        PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(filePath, publicKey , (Element) keyMap.get(PRIVATE_KEY));
        //查询
        String failPath = "/Users/yorick/Downloads/3.zip";
        QueryParam queryParam = algorithm.check(failPath, publicKey.getTypeAParams(), signList);
        //验证
        Boolean res = algorithm.verify(publicKey, queryParam);
        System.out.println(res);
    }
}
