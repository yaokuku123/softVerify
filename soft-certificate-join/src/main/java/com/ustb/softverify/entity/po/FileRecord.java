package com.ustb.softverify.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WYP
 * @date 2021-10-19 22:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FileRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String serverLocalName;
    private String serverLocalPath;
    private Integer govUserId;
}
