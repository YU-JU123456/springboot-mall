package com.ruby.mall.service;

import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.dto.OrderQueryParam;
import com.ruby.mall.model.Order;

import java.util.List;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);
    List<Order> getOrders(OrderQueryParam orderQueryParam);
    Integer countOrders(OrderQueryParam orderQueryParam);
}
