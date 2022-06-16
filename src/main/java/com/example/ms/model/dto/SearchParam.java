package com.example.ms.model.dto;

import lombok.Data;

@Data
public class SearchParam {
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Long parentId;
}
