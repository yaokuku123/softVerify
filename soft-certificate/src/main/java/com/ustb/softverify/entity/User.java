package com.ustb.softverify.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author WYP
 * @date 2021-10-08 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uid;
    private String uname;
    private String company;
    private String phone;

    private ArrayList<UploadInfo> uploadInfos = new ArrayList<>();
    private ArrayList<Soft> softs = new ArrayList<>();

}
