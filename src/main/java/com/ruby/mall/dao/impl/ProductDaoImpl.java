package com.ruby.mall.dao.impl;

import com.ruby.mall.dao.ProductDao;
import com.ruby.mall.dto.ProductQueryParam;
import com.ruby.mall.dto.ProductRequest;
import com.ruby.mall.model.Product;
import com.ruby.mall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, " +
                "image_url, price, stock, description, created_date, " +
                "last_modified_date FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if(productList.size() > 0){
            return productList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date) VALUES (:product_name, " +
                ":category, :image_url, :price, :stock, :description, :created_date, :last_modified_date)";

        Map<String, Object> map = new HashMap<>();
        map.put("product_name", productRequest.getProduct_name());
        map.put("category", productRequest.getCategory().name());
        map.put("image_url", productRequest.getImage_url());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :product_name, category = :category, image_url = :image_url, " +
                "price = :price, stock = :stock, description = :description, last_modified_date = :last_modified_date " +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("product_name", productRequest.getProduct_name());
        map.put("category", productRequest.getCategory().name());
        map.put("image_url", productRequest.getImage_url());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("last_modified_date", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    // 搜尋商品
    @Override
    public List<Product> getProducts(ProductQueryParam productQueryParam) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "last_modified_date, created_date FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();
        /*
         * 查詢條件
         */
        sql = addFilteringSql(sql, map, productQueryParam);

        /*
         * 排序
         * ORDER BY 只能用 "+" 拼接的方式, 不能用 ":" 參數傳遞
         */
        sql += " ORDER BY " + productQueryParam.getOrderBy() + " " + productQueryParam.getSort();

        /*
         * 分頁
         */
        // LIMIT & OFFSET 會拼接在 ORDER BY 後面
        sql += " LIMIT :limit OFFSET :offset"; // 有設預設值不用判斷 null
        map.put("limit",  productQueryParam.getLimit());
        map.put("offset", productQueryParam.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;
    }

    // 計算商品總數量
    @Override
    public Integer countProducts(ProductQueryParam productQueryParam) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, productQueryParam);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParam productQueryParam){
        /*
         * 查詢條件
         */
        if(productQueryParam.getCategory() != null){
            sql += " AND category = :category";
            map.put("category", productQueryParam.getCategory().name());
        }

        // % 符號不能放在:變數裡
        if(productQueryParam.getSearch() != null){
            sql += " AND product_name LIKE :product_name";
            map.put("product_name", '%'+productQueryParam.getSearch()+'%');
        }

        return sql;
    }
}
