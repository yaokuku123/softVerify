package com.ustb.softverify;

import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.vo.PublicKeyStr;
import com.ustb.softverify.domain.QueryParam;
import com.ustb.softverify.domain.vo.QueryParamStr;
import com.ustb.softverify.utils.PublicKeyTransferUtil;
import com.ustb.softverify.utils.QueryParamTransferUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TransferTest {

    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";

    @Test
    public void publicKeyAndQueryParamTransferTest() throws IOException, ClassNotFoundException {
        String filePath = "/Users/yorick/Downloads/auth-resource.jar";
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(filePath);
        //初始化
        Map<String, Object> keyMap = algorithm.initParams();
        //签名
        PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(filePath, publicKey , (Element) keyMap.get(PRIVATE_KEY));
        //查询
        QueryParam queryParam = algorithm.check(filePath, publicKey.getTypeAParams(), signList);
        QueryParamStr queryParamStr = QueryParamTransferUtil.encodeToStr(queryParam);
        QueryParam queryParamTrans = QueryParamTransferUtil.decodeToObj(queryParamStr);
        //验证
        PublicKeyStr publicKeyStr = PublicKeyTransferUtil.encodeToStr(publicKey);
        PublicKey publicKeyTrans = PublicKeyTransferUtil.decodeToObj(publicKeyStr);
        Boolean res = algorithm.verify(publicKeyTrans, queryParamTrans);
        System.out.println(res);
    }
}
