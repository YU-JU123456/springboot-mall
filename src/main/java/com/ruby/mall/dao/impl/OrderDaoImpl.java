package com.ruby.mall.dao.impl;

import com.ruby.mall.dao.OrderDao;
import com.ruby.mall.dto.OrderQueryParam;
import com.ruby.mall.model.Order;
import com.ruby.mall.model.OrderItem;
import com.ruby.mall.rowmapper.OrderItemRowMapper;
import com.ruby.mall.rowmapper.OrderRowMapper;
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

    @Override
    public Order getOrderByOrderId(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date FROM `order` WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
        if(orderList.size()>0){
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT oi.order_id, oi.order_item_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " +
                "FROM order_item as oi " +
                "LEFT JOIN product as p on oi.product_id = p.product_id " +
                "WHERE oi.order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItems = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
        return orderItems;
    }

    @Override
    public List<Order> getOrders(OrderQueryParam orderQueryParam) {
        String sql = "SELECT order_id, user_id, created_date, total_amount, " +
                "last_modified_date FROM `order` WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, orderQueryParam);

        // 排序
        sql += "ORDER BY created_date DESC ";

        // 分頁
        sql += "LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParam.getLimit());
        map.put("offset", orderQueryParam.getOffset());

        return namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
    }

    @Override
    public Integer countOrders(OrderQueryParam orderQueryParam) {
        String sql = "SELECT count(*) FROM `order` WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();
        sql = addFilteringSql(sql, map, orderQueryParam);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    private String addFilteringSql(String sql, Map map, OrderQueryParam orderQueryParam){
        sql += "AND user_id = :userId ";

        map.put("userId", orderQueryParam.getUserId());
        return sql;
    }
}
