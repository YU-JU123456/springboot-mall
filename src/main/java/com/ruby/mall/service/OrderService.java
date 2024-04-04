package com.ruby.mall.service;

import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.model.Order;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);
}
