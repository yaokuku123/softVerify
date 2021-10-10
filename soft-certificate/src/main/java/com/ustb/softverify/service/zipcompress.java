package com.ustb.softverify.service;

public interface zipcompress {
    void unzip(String zipFilePath,String desDirectory) throws Exception;
    void changeroot(String zipFilePath);
}
