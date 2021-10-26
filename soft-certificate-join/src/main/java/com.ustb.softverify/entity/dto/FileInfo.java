package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author WYP
 * @date 2021-10-14 14:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileInfo {

    private String softName;
    private String docName;
    private String filePath;
    private String excelPaths;

}
