package com.ustb.softverify.utils;

import java.util.Arrays;
import java.util.List;

public class ListStringUtils {
    public static String listToString(List<String> list) {
        return String.join(",", list);
    }

    public static List<String> stringToList(String str) {
        String[] strings = str.split(",");
        return Arrays.asList(strings);
    }
}
