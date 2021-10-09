package com.ustb.softverify.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class QueryParamStr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String typeAParams;
    private String sigmasValues;
    private ArrayList<String> viLists;
    private ArrayList<String> signLists;
    private ArrayList<String> miuLists;
}
