package com.ruby.mall.dao;

import com.ruby.mall.dto.ProductQueryParam;
import com.ruby.mall.dto.ProductRequest;
import com.ruby.mall.model.Product;

import java.util.List;

public interface ProductDao {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);

    // 搜尋商品
    List<Product> getProducts(ProductQueryParam productQueryParam);
    Integer countProducts(ProductQueryParam productQueryParam);

    void updateStock(Integer productId, Integer stock);
}
