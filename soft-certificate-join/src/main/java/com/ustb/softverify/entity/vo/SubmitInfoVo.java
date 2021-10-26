package com.ustb.softverify.entity.vo;

import com.ustb.softverify.entity.dto.SoftFileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer uid;
    private Integer govUserId;
    private String uname;
    private String company;
    private String phone;
    private Integer sid;
    private String softName;
    private String softDesc;

    /* 软件文档信息 */
    private List<SoftFileInfo> softFileList;
}
