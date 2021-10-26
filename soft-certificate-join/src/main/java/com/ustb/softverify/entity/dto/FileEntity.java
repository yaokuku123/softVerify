package com.ustb.softverify.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /* 编号 */
    @ExcelProperty(value = "编号", index = 0)
    private Integer number;
    /* 项目名称 */
    @ExcelProperty(value = "项目名称", index = 1)
    private String proName;
    /* 单位名称 */
    @ExcelProperty(value = "单位名称", index = 2)
    private String comName;
    /* 核验码 */
    @ExcelProperty(value = "核验码", index = 3)
    private String verificationCode;
    /* 标识 */
    @ExcelProperty(value = "证书编号", index = 4)
    private String pid;

}
