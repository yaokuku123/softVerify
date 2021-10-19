package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUploadInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uname;
    private String company;
    private String phone;
    private String softName;
    private String softDesc;
}
