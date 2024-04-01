package com.ruby.mall.service.impl;

import com.ruby.mall.dao.ProductDao;
import com.ruby.mall.dto.ProductQueryParam;
import com.ruby.mall.dto.ProductRequest;
import com.ruby.mall.model.Product;
import com.ruby.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    // CRUD
    @Override
    public void deleteProductbyId(Integer productId) {
        productDao.deleteProductById(productId);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    // 搜尋商品
    @Override
    public List<Product> getProducts(ProductQueryParam productQueryParam) {
        return productDao.getProducts(productQueryParam);
    }

    @Override
    public Integer countProducts(ProductQueryParam productQueryParam) {
        return productDao.countProducts(productQueryParam);
    }
}
