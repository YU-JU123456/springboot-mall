package com.ruby.mall.dao.impl;

import com.ruby.mall.dao.OrderDao;
import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.model.Order;
import com.ruby.mall.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, Integer tCount) {
        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) " +
                "VALUES (:userId, :tAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("tAmount", tCount);

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int orderId = keyHolder.getKey().intValue();
        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        // 使用 batchUpdate 實做效率比使用 for loop 高
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) VALUES (:orderId, :productId, :quantity, :amount)";
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for(int i = 0; i < orderItemList.size(); i++){
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId", orderId);
            parameterSources[i].addValue("productId", orderItem.getProductId());
            parameterSources[i].addValue("quantity", orderItem.getQuantity());
            parameterSources[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }
}
