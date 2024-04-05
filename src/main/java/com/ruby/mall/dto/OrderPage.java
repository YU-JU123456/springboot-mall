package com.ruby.mall.dto;

import java.util.List;

public class OrderPage<T> {
    private Integer total;
    private List<T> orders;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getOrders() {
        return orders;
    }

    public void setOrders(List<T> orders) {
        this.orders = orders;
    }
}
