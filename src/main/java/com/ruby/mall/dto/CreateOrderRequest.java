package com.ruby.mall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/*
    接收建立訂單的 api 時, 將 json 轉為 java obj
*/
public class CreateOrderRequest {
    @NotEmpty
    @Valid
    @JsonProperty("buyItemList")
    private List<BuyItem> orderList;

    public List<BuyItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<BuyItem> orderList) {
        this.orderList = orderList;
    }
}
