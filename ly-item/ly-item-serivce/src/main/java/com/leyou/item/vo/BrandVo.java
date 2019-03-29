package com.leyou.item.vo;

import lombok.Data;

import java.util.List;

@Data
public class BrandVo {

    private Long id;

    private String name;

    private String image;

    private Character letter;

    private List<Long> categories;
}
