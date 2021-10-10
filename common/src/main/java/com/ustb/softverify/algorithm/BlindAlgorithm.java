package com.ustb.softverify.algorithm;

import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.QueryParam;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     *
     * @param filePath 文件路径
     * @return 哈希结果
     * @throws FileNotFoundException
     */
    byte[] SM3Encrypt(String filePath) throws FileNotFoundException;

    /**
     * 采用密钥加密
     * @param key 密钥
     * @param filePath 文件路径
     * @return 哈希结果
     */
    byte[] hmac(byte[] key, String filePath) throws IOException;

    /**
     * 验证
     * @param filePath 验证文件所在路径
     * @param sm3HexString 原文件哈希值
     * @return 验证结果
     */
    boolean verify(String filePath, String sm3HexString);
}
