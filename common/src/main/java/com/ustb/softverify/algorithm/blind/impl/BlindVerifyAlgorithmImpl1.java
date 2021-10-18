package com.ustb.softverify.algorithm.blind.impl;

import com.ustb.softverify.BlindVerify.Check;
import com.ustb.softverify.BlindVerify.Sign;
import com.ustb.softverify.BlindVerify.Verify;
import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.QueryParam;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.utils.FileUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlindVerifyAlgorithmImpl1 implements BlindAlgorithm {
    private static final int R_BITS = 53;
    private static final int Q_BITS = 1024;
    private final int blockFileSize;
    private final int pieceFileSize;

    public BlindVerifyAlgorithmImpl1(String filePath) {
        //初始化配置 默认规定为 100块，每块有10片
        File file = new File(filePath);
        long originFileSize = file.length();
        if (originFileSize < 10240) {
            originFileSize += 10240;
        }
        blockFileSize = (int) (originFileSize / 100);// 切割后的文件块大小
        pieceFileSize = blockFileSize / 10;// 切割后的文件片大小
    }

    @Override
    public Map<String,Object> initParams() {
        TypeACurveGenerator pg = new TypeACurveGenerator(R_BITS, Q_BITS);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);
        //初始化相关参数
        Element g = pairing.getG1().newRandomElement().getImmutable();     //生成生成元
        Element x = pairing.getZr().newRandomElement().getImmutable();
        Element v = g.powZn(x);
        //生成U
        ArrayList<ElementPowPreProcessing> uLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            uLists.add(pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing());
        }
        Map<String,Object> map = new HashMap<>();
        map.put(PUBLIC_KEY, new PublicKey(typeAParams, g, v, uLists));
        map.put(PRIVATE_KEY,x);
        return map;
    }

    @Override
    public ArrayList<Element> sign(String filePath, PublicKey publicKey, Element privateKey) {
        //签名阶段
        ArrayList<Element> signList;
        Sign sign = new Sign();
        FileUtil fileUtil = new FileUtil();
        //判断文件大小是否不满足算法要求，不满足要求则补充字节并创建临时文件
        boolean flag = false;
        if (new File(filePath).length() < 10240) {
            flag = true;
            filePath = supplyFile(filePath);
        }
        signList = sign.sign(fileUtil, filePath, publicKey.getULists(), publicKey.getG(),privateKey, blockFileSize, pieceFileSize);
        //删除临时文件
        if (flag) {
            deleteFile(filePath);
        }
        return signList;
    }

    @Override
    public QueryParam check(String filePath, PairingParameters typeAParams, ArrayList<Element> signList) {
        //获取生成源
        Pairing pairing = PairingFactory.getPairing(typeAParams);
        //查询阶段
        Check check = new Check();
        //求viLists
        ArrayList<Element> viLists;
        viLists = check.getViList(pairing, signList);
        //求sigma
        Element sigmasValues = check.getSigh(pairing, signList, viLists);
        //求miu
        ArrayList<Element> miuLists;
        FileUtil fileUtil = new FileUtil();
        //判断文件大小是否不满足算法要求，不满足要求则补充字节并创建临时文件
        boolean flag = false;
        if (new File(filePath).length() < 10240) {
            flag = true;
            filePath = supplyFile(filePath);
        }
        miuLists = check.getMiuList(fileUtil, filePath, viLists, blockFileSize, pieceFileSize);
        //删除临时文件
        if (flag) {
            deleteFile(filePath);
        }
        QueryParam queryParam = new QueryParam(typeAParams,sigmasValues,viLists,signList,miuLists);
        return queryParam;
    }

    @Override
    public Boolean verify(PublicKey publicKey, QueryParam queryParam) {
        Verify verify = new Verify();
        Pairing pairing = queryParam.getPairing();
        Element sigmasValues = queryParam.getSigmasValues();
        ArrayList<Element> signLists = queryParam.getSignLists();
        ArrayList<Element> viLists = queryParam.getViLists();
        ArrayList<Element> miuLists = queryParam.getMiuLists();
        Element g = publicKey.getG();
        Element v = publicKey.getV();
        ArrayList<ElementPowPreProcessing> uLists = publicKey.getULists();
        return verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
    }

    /**
     * 删除临时文件
     * @param filePath 文件路径
     */
    private void deleteFile(String filePath) {
        FileUtil.delete(filePath);
    }

    /**
     * 补充字符方法
     * @param source 原始文件路径
     * @return 临时文件路径
     */
    private String supplyFile(String source) {
        String dest = source + ".tmp";
        try {
            byte[] bytes = FileUtil.getBytes(new File(source));
            byte[] newBytes = new byte[bytes.length + 10240];
            System.arraycopy(bytes,0,newBytes,0,bytes.length);
            for (int i = bytes.length; i < bytes.length + 10240; i++) {
                newBytes[i] = 'a';
            }
            FileUtil.createFile(newBytes,dest);
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileReadWriteException();
        }
    }
}
