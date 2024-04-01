package com.ruby.mall.dto;

import com.ruby.mall.constant.ProductCategory;
import jakarta.validation.constraints.NotNull;

/*
 * 將 json 轉為 java obj 存進 DB 的資料內容
 * */
public class ProductRequest {
    @NotNull
    private String product_name;
    @NotNull
    private ProductCategory category;
    @NotNull
    private String image_url;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
    private String description;


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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