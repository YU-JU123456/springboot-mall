package com.ruby.mall.dto;

import jakarta.validation.constraints.NotNull;

/*
    接收建立訂單的 api 時, 將單筆商品從 json 轉為 java obj
*/
public class BuyItem {
    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
