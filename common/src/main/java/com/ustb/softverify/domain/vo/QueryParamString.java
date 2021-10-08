package com.ustb.softverify.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryParamString {
    private String sigmasValues;
    private ArrayList<String> viLists;
    private ArrayList<String> signLists;
    private ArrayList<String> miuLists;
}
