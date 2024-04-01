package com.ruby.mall.rowmapper;

import com.ruby.mall.constant.ProductCategory;
import com.ruby.mall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();

        product.setProduct_id(rs.getInt("product_id"));
        product.setProduct_name(rs.getString("product_name"));

        String s = rs.getString("category");
        ProductCategory category = ProductCategory.valueOf(s);
        product.setCategory(category);

        product.setImage_url(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setDescription(rs.getString("description"));
        product.setCreated_date(rs.getTimestamp("created_date"));
        product.setLast_modified_date(rs.getTimestamp("last_modified_date"));

        return product;
    }
}
