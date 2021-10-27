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
    /* 項目信息 */
    @ExcelProperty(value = "项目信息", index = 1)
    private String project;
    /* 业务号 */
    @ExcelProperty(value = "业务号", index = 2)
    private String sysId;
    /* 申报单位 */
    @ExcelProperty(value = "申报单位", index = 3)
    private String appliedinst;
    /* 建设单位 */
    @ExcelProperty(value = "建设单位", index = 4)
    private String developinst;
    /* 核验码 */
    @ExcelProperty(value = "核验码", index = 5)
    private String verificationCode;
    /* 标识 */
    @ExcelProperty(value = "证书编号", index = 6)
    private String pid;

}
