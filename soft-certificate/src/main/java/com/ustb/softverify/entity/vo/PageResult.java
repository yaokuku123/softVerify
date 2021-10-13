package com.ustb.softverify.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page;//分页起始页
    private Integer size;//每页记录数
    private List<T> rows;//返回的记录集合
    private Long total;//总记录条数
}

