package com.ruby.mall.controller;

import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.dto.OrderPage;
import com.ruby.mall.dto.OrderQueryParam;
import com.ruby.mall.exception.MallException;
import com.ruby.mall.model.Order;
import com.ruby.mall.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Object> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest)
    {
        try{
            Integer orderId = orderService.createOrder(userId, createOrderRequest);
            Object order = orderService.getOrderById(orderId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (MallException e){
            return ResponseEntity.status(e.responseCode).body(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<OrderPage<Order>> getOrder(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Min(0) @Max(1000) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setUserId(userId);
        orderQueryParam.setOffset(offset);
        orderQueryParam.setLimit(limit);

        List<Order> orderList = orderService.getOrders(orderQueryParam);
        Integer count = orderService.countOrders(orderQueryParam);

        OrderPage<Order> page = new OrderPage<>();
        page.setTotal(count);
        page.setOrders(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
