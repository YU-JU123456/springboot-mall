package com.ruby.mall.service.impl;

import com.ruby.mall.dao.OrderDao;
import com.ruby.mall.dao.ProductDao;
import com.ruby.mall.dto.BuyItem;
import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.model.OrderItem;
import com.ruby.mall.model.Product;
import com.ruby.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional // 確保會成功更新兩張 tb, 一個失敗就會一起失敗
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int tAcount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getOrderList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            int amount = product.getPrice() * buyItem.getQuantity();
            tAcount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId, tAcount);
        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }
}
