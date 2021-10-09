package com.ustb.softverify.domain;

import com.ustb.softverify.utils.SerializeUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: QueryParam
 * Author: yaoqijun
 * Date: 2021/6/25 8:52
 */
@Data
public class QueryParam implements Serializable {
    private PairingParameters typeAParams;
    private Pairing pairing;
    private Element sigmasValues;
    private ArrayList<Element> viLists;
    private ArrayList<Element> signLists;
    private ArrayList<Element> miuLists;

    public QueryParam(PairingParameters typeAParams, Element sigmasValues, ArrayList<Element> viLists, ArrayList<Element> signLists, ArrayList<Element> miuLists) {
        this.typeAParams = typeAParams;
        this.pairing = PairingFactory.getPairing(typeAParams);
        this.sigmasValues = sigmasValues;
        this.viLists = viLists;
        this.signLists = signLists;
        this.miuLists = miuLists;
    }

}
