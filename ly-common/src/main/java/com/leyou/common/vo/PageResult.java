package com.leyou.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 通用分页对象
 * @param <T>
 */
@Data
public class PageResult<T> {

    private Long total; //总条数
    private Integer totalPage;  //总页数
    private List<T> items; //页数据


    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
