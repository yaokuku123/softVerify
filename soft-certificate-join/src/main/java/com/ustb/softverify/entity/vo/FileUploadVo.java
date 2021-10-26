package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private Integer fileType;
}
