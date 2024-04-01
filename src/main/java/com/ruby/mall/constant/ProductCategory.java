package com.ruby.mall.constant;

public enum ProductCategory {
    FOOD("食物"),
    CAR("車子"),
    E_BOOK("書");

    private String chineseName = null;
    ProductCategory(String s) {
        this.chineseName = s;
    }

    public String getChineseName() {
        return chineseName;
    }
}
