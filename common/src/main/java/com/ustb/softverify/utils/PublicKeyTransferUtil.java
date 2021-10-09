package com.ustb.softverify.utils;

import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.vo.PublicKeyStr;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PublicKeyTransferUtil {

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 由公钥转换为字符串公钥
     * @param publicKey 公钥对象
     * @return 字符串公钥对象
     * @throws IOException
     */
    public static PublicKeyStr encodeToStr(PublicKey publicKey) throws IOException {
        PairingParameters typeAParams = publicKey.getTypeAParams();
        Element g = publicKey.getG();
        Element v = publicKey.getV();
        ArrayList<ElementPowPreProcessing> uLists = publicKey.getULists();
        return new PublicKeyStr(encodeTypeAParams(typeAParams),encodeG(g),encodeV(v),encodeULists(uLists));
    }

    /**
     * 由字符串公钥转换为公钥
     * @param publicKeyStr 字符串公钥
     * @return 公钥对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static PublicKey decodeToObj(PublicKeyStr publicKeyStr) throws IOException, ClassNotFoundException {
        String typeAParamsStr = publicKeyStr.getTypeAParams();
        String g = publicKeyStr.getG();
        String v = publicKeyStr.getV();
        List<String> uLists = publicKeyStr.getULists();
        PairingParameters typeAParams = decodeTypeAParams(typeAParamsStr);
        Pairing pairing = PairingFactory.getPairing(typeAParams);
        return new PublicKey(typeAParams,decodeG(pairing,g),decodeV(pairing,v),decodeULists(pairing,uLists));
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
     * 使用Base64编码的方式将Element类型的G转换为String类型
     *
     * @return
     */
    private static String encodeG(Element g) throws UnsupportedEncodingException {
        byte[] gByte = encoder.encode(g.toBytes());
        String gString = new String(gByte, "UTF-8");
        return gString;
    }

    /**
     * 使用Base64编码的方式将Element类型的V转换为String类型
     *
     * @return
     */
    private static String encodeV(Element v) throws UnsupportedEncodingException {
        byte[] vByte = encoder.encode(v.toBytes());
        String vString = new String(vByte, "UTF-8");
        return vString;
    }

    /**
     * 使用Base64编码的方式将ElementPowPreProcessing转换为String类型
     *
     * @return
     */
    private static ArrayList<String> encodeULists(ArrayList<ElementPowPreProcessing> uLists) throws UnsupportedEncodingException {
        ArrayList<String> uStringList = new ArrayList<>();
        for (ElementPowPreProcessing elm : uLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            String elmString = new String(elmByte, "UTF-8");
            uStringList.add(elmString);
        }
        return uStringList;
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
     * 使用Base64编码的方式将String类型的G转换为Element类型
     *
     * @return
     */
    private static Element decodeG(Pairing pairing, String gString) throws UnsupportedEncodingException {
        byte[] gByte = decoder.decode(gString.getBytes("UTF-8"));
        return pairing.getG1().newElementFromBytes(gByte).getImmutable();
    }

    /**
     * 使用Base64编码的方式将String类型的V转换为Element类型
     *
     * @return
     */
    private static Element decodeV(Pairing pairing, String vString) throws UnsupportedEncodingException {
        byte[] vByte = decoder.decode(vString.getBytes("UTF-8"));
        return pairing.getG1().newElementFromBytes(vByte).getImmutable();
    }

    /**
     * 使用Base64编码的方式将String类型的uList转换为ElementPowPreProcessing类型
     *
     * @return
     */
    private static ArrayList<ElementPowPreProcessing> decodeULists(Pairing pairing, List<String> uStringList) throws UnsupportedEncodingException {
        ArrayList<ElementPowPreProcessing> uLists = new ArrayList<>();
        for (String elm : uStringList) {
            byte[] elmByte = decoder.decode(elm.getBytes("UTF-8"));
            ElementPowPreProcessing elmE = pairing.getG1().newRandomElement().getImmutable().getField().getElementPowPreProcessingFromBytes(elmByte);
            uLists.add(elmE);
        }
        return uLists;
    }
}
