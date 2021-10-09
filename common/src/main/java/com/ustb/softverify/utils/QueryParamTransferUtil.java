package com.ustb.softverify.utils;


import com.ustb.softverify.domain.QueryParam;
import com.ustb.softverify.domain.vo.QueryParamStr;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class QueryParamTransferUtil {

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 由查询参数对象转为查询参数字符串
     * @param queryParam 查询参数对象
     * @return 查询参数字符串
     * @throws IOException
     */
    public static QueryParamStr encodeToStr(QueryParam queryParam) throws IOException {
        PairingParameters typeAParams = queryParam.getTypeAParams();
        Element sigmasValues = queryParam.getSigmasValues();
        ArrayList<Element> viLists = queryParam.getViLists();
        ArrayList<Element> signLists = queryParam.getSignLists();
        ArrayList<Element> miuLists = queryParam.getMiuLists();
        return new QueryParamStr(encodeTypeAParams(typeAParams),encodeSigma(sigmasValues),
                encodeViList(viLists),encodeSignLists(signLists),encodeMiuLists(miuLists));
    }

    /**
     * 由查询参数字符串转为查询参数对象
     * @param queryParamStr 查询参数字符串
     * @return 查询参数对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static QueryParam decodeToObj(QueryParamStr queryParamStr) throws IOException, ClassNotFoundException {
        String typeAParamsStr = queryParamStr.getTypeAParams();
        String sigmasValues = queryParamStr.getSigmasValues();
        ArrayList<String> viLists = queryParamStr.getViLists();
        ArrayList<String> signLists = queryParamStr.getSignLists();
        ArrayList<String> miuLists = queryParamStr.getMiuLists();
        PairingParameters typeAParams = decodeTypeAParams(typeAParamsStr);
        Pairing pairing = PairingFactory.getPairing(typeAParams);
        return new QueryParam(typeAParams,decodeSigma(pairing,sigmasValues),decodeViLists(pairing,viLists),
                decodeSignLists(pairing,signLists),decodeMiuLists(pairing,miuLists));
    }


    /**
     * 使用Base64编码的方式将PairingParameters类型的typeAParams转换为String类型
     *
     * @return
     */
    private static String encodeTypeAParams(PairingParameters typeAParams) throws IOException {
        return new String(encoder.encode(SerializeUtil.serialize(typeAParams)
                .getBytes("UTF-8")),"UTF-8");
    }

    /**
     * 使用Base64编码的方式将Element类型的sigmasValues转换为String类型
     *
     * @return
     */
    private static String encodeSigma(Element sigmasValues) throws UnsupportedEncodingException {
        byte[] sigmaByte = encoder.encode(sigmasValues.toBytes());
        String sigmaString = new String(sigmaByte, "UTF-8");
        return sigmaString;
    }

    /**
     * 使用Base64编码的方式将viLists转换为String类型
     *
     * @return
     */
    private static ArrayList<String> encodeViList(List<Element> viLists) throws UnsupportedEncodingException {
        ArrayList<String> viStringList = new ArrayList<>();
        for (Element elm : viLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            viStringList.add(elmString);
        }
        return viStringList;
    }

    /**
     * 使用Base64编码的方式将viLists转换为String类型
     *
     * @return
     */
    private static ArrayList<String> encodeSignLists(List<Element> signLists) throws UnsupportedEncodingException {
        ArrayList<String> signStringList = new ArrayList<>();
        for (Element elm : signLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            signStringList.add(elmString);
        }
        return signStringList;
    }

    /**
     * 使用Base64编码的方式将miuLists转换为String类型
     *
     * @return
     */
    private static ArrayList<String> encodeMiuLists(List<Element> miuLists) throws UnsupportedEncodingException {
        ArrayList<String> miuStringList = new ArrayList<>();
        for (Element elm : miuLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            miuStringList.add(elmString);
        }
        return miuStringList;
    }

    /**
     * 使用Base64编码的方式将String类型的typeAParams转换为PairingParameters类型
     *
     * @return
     */
    private static PairingParameters decodeTypeAParams(String typeAParamsString) throws IOException, ClassNotFoundException {
        return  (PairingParameters) SerializeUtil.serializeToObject(new String(decoder.decode(typeAParamsString.getBytes("UTF-8")),"UTF-8"));
    }

    /**
     * 使用Base64编码的方式将String类型的sigmasValues转换为Element类型
     *
     * @return
     */
    private static Element decodeSigma(Pairing pairing, String sigmaString) throws UnsupportedEncodingException {
        byte[] sigmaByte = decoder.decode(sigmaString.getBytes("UTF-8"));
        return pairing.getG1().newElementFromBytes(sigmaByte).getImmutable();
    }

    /**
     * 使用Base64编码的方式将String类型的viLists转换
     *
     * @return
     */
    private static ArrayList<Element> decodeViLists(Pairing pairing, List<String> viStringList) throws UnsupportedEncodingException {
        ArrayList<Element> viLists = new ArrayList<>();
        for (String elm : viStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            Element elmE = pairing.getZr().newElementFromBytes(elmByte).getImmutable();
            viLists.add(elmE);
        }
        return viLists;
    }

    /**
     * 使用Base64编码的方式将String类型的signLists转换
     *
     * @return
     */
    private static ArrayList<Element> decodeSignLists(Pairing pairing, List<String> signStringList) throws UnsupportedEncodingException {
        ArrayList<Element> signLists = new ArrayList<>();
        for (String elm : signStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            Element elmE = pairing.getG1().newElementFromBytes(elmByte).getImmutable();
            signLists.add(elmE);
        }
        return signLists;
    }

    /**
     * 使用Base64编码的方式将String类型的miuLists转换
     *
     * @return
     */
    private static ArrayList<Element> decodeMiuLists(Pairing pairing, List<String> miuStringList) throws UnsupportedEncodingException {
        ArrayList<Element> miuLists = new ArrayList<>();
        for (String elm : miuStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            Element elmE = pairing.getZr().newElementFromBytes(elmByte).getImmutable();
            miuLists.add(elmE);
        }
        return miuLists;
    }
}
