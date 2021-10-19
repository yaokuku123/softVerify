package com.ustb.softverify.entity.dto;

public class CompInfo {
    private String fileName;
    private String fileSize;

    public boolean isFlag() {
        return flag;
    }

    public boolean setFlag(boolean flag1) {
        flag = flag1;
        return this.flag;
    }

    private boolean flag;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public CompInfo(String fileName, String fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.flag = false;
    }

}
