package com.ustb.softverify.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author WYP
 * @date 2021-10-24 19:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PdfTemplete {

    private Integer certId;
    private String appName;
    private String softName;
    private String softVersion;
    private String date;
    private String softUi;
}
