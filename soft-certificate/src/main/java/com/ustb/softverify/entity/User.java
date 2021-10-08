package com.ustb.softverify.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author WYP
 * @date 2021-10-08 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uid;
    private String uname;
    private String company;
    private String phone;

    private ArrayList<UploadInfo> uploadInfos = new ArrayList<>();
    private ArrayList<SoftInfo> softInfos = new ArrayList<>();

}
