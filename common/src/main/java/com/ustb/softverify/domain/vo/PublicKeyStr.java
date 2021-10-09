package com.ustb.softverify.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class PublicKeyStr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String typeAParams;
    private String g;
    private String v;
    private ArrayList<String> uLists;
}
