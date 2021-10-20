package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompInfo {
    private String orgName;
    private Long fileSize;
    private boolean flag;

    public CompInfo(String orgName,Long fileSize) {
        this.orgName = orgName;
        this.fileSize = fileSize;
    }
}
