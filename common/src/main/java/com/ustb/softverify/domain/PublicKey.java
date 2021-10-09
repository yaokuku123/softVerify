package com.ustb.softverify.domain;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: PublicKey
 * Author: yaoqijun
 * Date: 2021/6/24 15:48
 */
@Data
public class PublicKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private PairingParameters typeAParams;
    private Pairing pairing;
    private Element g;
    private Element v;
    private ArrayList<ElementPowPreProcessing> uLists;

    public PublicKey(PairingParameters typeAParams, Element g, Element v, ArrayList<ElementPowPreProcessing> uLists) {
        this.typeAParams = typeAParams;
        this.pairing = PairingFactory.getPairing(typeAParams);
        this.g = g;
        this.v = v;
        this.uLists = uLists;
    }
}
