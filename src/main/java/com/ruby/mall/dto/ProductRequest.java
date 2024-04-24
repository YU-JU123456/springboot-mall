package com.ruby.mall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruby.mall.constant.ProductCategory;
import jakarta.validation.constraints.NotNull;

/*
 * 將 json 轉為 java obj 存進 DB 的資料內容
 * */
public class ProductRequest {
    @NotNull
    @JsonProperty("product_name")
    private String productName;
    @NotNull
    private ProductCategory category;
    @NotNull
    @JsonProperty("image_url")
    private String imageUrl;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
    private String description;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
