package com.ruby.mall.service;

import com.ruby.mall.dto.ProductQueryParam;
import com.ruby.mall.dto.ProductRequest;
import com.ruby.mall.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductbyId(Integer productId);

    // 搜尋商品
    List<Product> getProducts(ProductQueryParam productQueryParam);
    Integer countProducts(ProductQueryParam productQueryParam);
}
