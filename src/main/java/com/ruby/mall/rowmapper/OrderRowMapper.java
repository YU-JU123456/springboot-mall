package com.ruby.mall.rowmapper;

import com.ruby.mall.model.Order;
import com.ruby.mall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();

        order.setOrderId(rs.getInt("order_id"));
        order.setCreateDate(rs.getTimestamp("created_date"));
        order.settAmount(rs.getInt("total_amount"));
        order.setUserId(rs.getInt("user_id"));
        order.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return order;
    }
}
