package com.ustb.softverify.algorithm;

import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.QueryParam;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;

import java.util.ArrayList;
import java.util.Map;

public interface BlindAlgorithm {

    /**
     * 初始化算法参数阶段
     * @return 公私钥信息
     */
    Map<String,Object> initParams();

    /**
     * 签名阶段
     * @param filePath 文件路径
     * @param publicKey 公钥信息
     * @param privateKey 私钥信息
     * @return 签名列表
     */
    ArrayList<Element> sign(String filePath, PublicKey publicKey, Element privateKey);

    /**
     * 查询阶段
     * @param filePath 文件路径
     * @param typeAParams 生成源
     * @param signList 签名列表
     * @return 查询参数
     */
    QueryParam check(String filePath, PairingParameters typeAParams, ArrayList<Element> signList);

    /**
     * 验证阶段
     * @param publicKey 公钥信息
     * @param queryParam 查询参数
     * @return 验证结果
     */
    Boolean verify(PublicKey publicKey, QueryParam queryParam);

}
