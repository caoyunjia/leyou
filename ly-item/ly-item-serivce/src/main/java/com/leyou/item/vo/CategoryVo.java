package com.leyou.item.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.leyou.item.pojo.Category;
import lombok.Data;

import java.util.List;
@Data
@JsonSerialize()
public class CategoryVo extends Category {
    private List<String> path;
    private List<String> children;
}
