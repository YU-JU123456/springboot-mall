package com.ruby.mall.service.impl;

import com.ruby.mall.constant.StatusCode;
import com.ruby.mall.dao.OrderDao;
import com.ruby.mall.dao.ProductDao;
import com.ruby.mall.dao.UserDao;
import com.ruby.mall.dto.BuyItem;
import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.dto.OrderQueryParam;
import com.ruby.mall.exception.MallException;
import com.ruby.mall.model.Order;
import com.ruby.mall.model.OrderItem;
import com.ruby.mall.model.Product;
import com.ruby.mall.model.User;
import com.ruby.mall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class); // 要選 slf4j 的 logger

    @Override
    @Transactional // 確保會成功更新兩張 tb, 一個失敗就會一起失敗
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) throws MallException {
        User user = userDao.getUserById(userId);
        if (user == null){
            log.warn("userId {} 不存在", userId);
            throw new MallException(StatusCode.ORDER_USER_NOT_EXIST);
        }

        int tAcount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getOrderList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            // 檢查商品是否存在 & 庫存是否足夠
            if(product == null){
                log.warn("商品 id {} 不存在", buyItem.getProductId());
                throw new MallException(StatusCode.ORDER_PRODUCT_NOT_EXIST);
            }

            if(buyItem.getQuantity() > product.getStock()){
                log.warn("商品 id {} 庫存不足, 庫存剩餘 {}, 需要數量 {}", buyItem.getProductId(),product.getStock(), buyItem.getQuantity());
                throw new MallException(StatusCode.ORDER_STOCK_LIMITED);
            }

            int amount = product.getPrice() * buyItem.getQuantity();
            tAcount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);

            // 扣除商品庫存
            productDao.updateStock(buyItem.getProductId(), product.getStock() - buyItem.getQuantity());
        }

        Integer orderId = orderDao.createOrder(userId, tAcount);
        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderByOrderId(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
        order.setOrderItemList(orderItemList);
        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryParam orderQueryParam) {
        List<Order> orderList = orderDao.getOrders(orderQueryParam);

        for (Order order: orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }

    @Override
    public Integer countOrders(OrderQueryParam orderQueryParam) {
        return orderDao.countOrders(orderQueryParam);
    }
}
