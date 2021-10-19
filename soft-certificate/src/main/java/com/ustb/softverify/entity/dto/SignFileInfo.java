package com.ustb.softverify.entity.dto;

import cn.hutool.core.lang.Chain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SignFileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serverLocalName;
    private String serverLocalPath;

}
