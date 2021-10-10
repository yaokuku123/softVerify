package com.ustb.softverify.service;

public interface ZipCompress {
    String unzip(String zipFilePath,String desDirectory) throws Exception;
    void changeroot(String zipFilePath);
}
