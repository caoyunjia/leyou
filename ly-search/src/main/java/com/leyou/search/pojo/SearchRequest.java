package com.leyou.search.pojo;


import java.util.Map;

public class SearchRequest {
    private String key;
    private Integer page;
    private Integer size;
    private static final Integer DEFAULT_ROWS=20;
    private static final Integer DEFAULT_PAGE=1;

    private Map<String, String> filter;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null) {
            page=DEFAULT_PAGE;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public static Integer getDefaultRows() {
        return DEFAULT_ROWS;
    }

    public static Integer getDefaultPage() {
        return DEFAULT_PAGE;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }
}
