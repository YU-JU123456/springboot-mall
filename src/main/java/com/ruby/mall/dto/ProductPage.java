package com.ruby.mall.dto;

import java.util.List;

public class ProductPage<T> {
    private Integer total;
    private List<T> results;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
