package com.ustb.softverify.entity.dto;

import com.ustb.softverify.domain.vo.PublicKeyStr;
import com.ustb.softverify.entity.po.SoftInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private PublicKeyStr publicKeyStr;
    private SoftInfo softInfo;
}
