package com.ustb.softverify.service;

import java.util.List;
import java.util.Map;

public interface ControlExcel {
    List<Map<String,String>> redExcel(String filePath) throws Exception;
}
