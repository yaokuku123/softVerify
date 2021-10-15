package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignFileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileSize;
    private String govUserId;
    private String createTime;
}
