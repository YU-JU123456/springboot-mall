package com.ruby.mall.dao;

import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.dto.OrderPage;
import com.ruby.mall.dto.OrderQueryParam;
import com.ruby.mall.model.Order;
import com.ruby.mall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId, Integer tCount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
    Order getOrderByOrderId(Integer orderId);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    List<Order> getOrders(OrderQueryParam orderQueryParam);
    Integer countOrders(OrderQueryParam orderQueryParam);
}
