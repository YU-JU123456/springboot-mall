package com.ruby.mall.controller;

import com.ruby.mall.constant.ProductCategory;
import com.ruby.mall.dto.ProductPage;
import com.ruby.mall.dto.ProductQueryParam;
import com.ruby.mall.dto.ProductRequest;
import com.ruby.mall.model.Product;
import com.ruby.mall.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // CRUD
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);
        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        // 可另外建一個 class 接住 request, 方便增加檢查 notNull 設定
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){

        Product product = productService.getProductById(productId);
        if (product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productService.updateProduct(productId, productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProductbyId(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 查詢列表
    @GetMapping("/products")
    public ResponseEntity<ProductPage<Product>> getProducts(
            // filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,

            // sort
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁
            @RequestParam(defaultValue = "5") @Max(100) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setCategory(category);
        productQueryParam.setSearch(search);

        productQueryParam.setOrderBy(orderBy);
        productQueryParam.setSort(sort);

        productQueryParam.setLimit(limit);
        productQueryParam.setOffset(offset);

        List<Product> productList = productService.getProducts(productQueryParam);
        Integer count = productService.countProducts(productQueryParam);

        ProductPage<Product> page = new ProductPage<>();
        page.setTotal(count);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

}
