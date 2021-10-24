package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer fid;
    private String fileName;
    private String filePath;
    private Integer fileType;
    private Long fileSize;
    private String pid;
}
