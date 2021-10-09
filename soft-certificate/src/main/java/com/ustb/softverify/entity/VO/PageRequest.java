package com.ustb.softverify.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    //当前页码
    private Integer pageNum;
    //每页数量
    private Integer pageSize;
}
