package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoBackVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pid;
    private String comName;
    private String proName;
    private String verificationCode;
    private Integer status;

    private List<FileUploadVo> fileUploadVoList;
}
