package com.ruby.mall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruby.mall.common.CheckResult;
import com.ruby.mall.constant.StatusCode;
import com.ruby.mall.dto.BuyItem;
import com.ruby.mall.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 創建訂單
    @Transactional
    @Test
    public void createOrder_success() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(5);
        buyItemList.add(buyItem1);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(2);
        buyItemList.add(buyItem2);

        createOrderRequest.setOrderList(buyItemList);
        RequestBuilder requestBuilder = createOrder(createOrderRequest, 1, "user1@gmail.com", "user1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.orderId", notNullValue()))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.tAmount", equalTo(750)))
                .andExpect(jsonPath("$.orderItemList", hasSize(2)))
                .andExpect(jsonPath("$.createDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void createOrder_illegalArgument_emptyBuyItemList() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();
        createOrderRequest.setOrderList(buyItemList);

        RequestBuilder requestBuilder = createOrder(createOrderRequest, 1, "user1@gmail.com", "user1");
        new CheckResult(mockMvc).check(requestBuilder, 400);
    }

    @Transactional
    @Test
    public void createOrder_userNotExist() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        createOrderRequest.setOrderList(buyItemList);
        RequestBuilder requestBuilder = createOrder(createOrderRequest, 100, "user1@gmail.com", "user1");

        StatusCode statusCode = StatusCode.ORDER_USER_NOT_EXIST;
        new CheckResult(mockMvc).check(requestBuilder, statusCode.getResponseCode(), statusCode.getResponseBody());
    }

    @Transactional
    @Test
    public void createOrder_productNotExist() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(100);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        createOrderRequest.setOrderList(buyItemList);
        RequestBuilder requestBuilder = createOrder(createOrderRequest, 1, "user1@gmail.com", "user1");

        StatusCode statusCode = StatusCode.ORDER_PRODUCT_NOT_EXIST;
        new CheckResult(mockMvc).check(requestBuilder, statusCode.getResponseCode(), statusCode.getResponseBody());
    }

    @Transactional
    @Test
    public void createOrder_stockNotEnough() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(10000);
        buyItemList.add(buyItem1);

        createOrderRequest.setOrderList(buyItemList);
        RequestBuilder requestBuilder = createOrder(createOrderRequest, 1, "user1@gmail.com", "user1");

        StatusCode statusCode = StatusCode.ORDER_STOCK_LIMITED;
        new CheckResult(mockMvc).check(requestBuilder, statusCode.getResponseCode(), statusCode.getResponseBody());
    }

    @Test
    public void createOrder_401() throws Exception { // 不是本人下訂單
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(100);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        createOrderRequest.setOrderList(buyItemList);
        RequestBuilder requestBuilder = createOrder(createOrderRequest, 1, "user2@gmail.com", "user2");

        StatusCode statusCode = StatusCode.AUTHENTICATION_ERROR_PERSON;
        new CheckResult(mockMvc).check(requestBuilder, statusCode.getResponseCode(), statusCode.getResponseBody());
    }

    @Test
    public void createOrder_noCsrf() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(100);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        createOrderRequest.setOrderList(buyItemList);
        String json = objectMapper.writeValueAsString(createOrderRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .with(httpBasic("user2@gmail.com", "user2"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        new CheckResult(mockMvc).check(requestBuilder, 403);
    }

    // 查詢訂單列表
    @Test
    public void getOrders() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1)
                .with(httpBasic("test1@gmail.com", "111"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.orders", hasSize(2)))
                .andExpect(jsonPath("$.orders[0].orderId", notNullValue()))
                .andExpect(jsonPath("$.orders[0].userId", equalTo(1)))
                .andExpect(jsonPath("$.orders[0].tAmount", equalTo(100000)))
                .andExpect(jsonPath("$.orders[0].orderItemList", hasSize(1)))
                .andExpect(jsonPath("$.orders[0].createDate", notNullValue()))
                .andExpect(jsonPath("$.orders[0].lastModifiedDate", notNullValue()))
                .andExpect(jsonPath("$.orders[1].orderId", notNullValue()))
                .andExpect(jsonPath("$.orders[1].userId", equalTo(1)))
                .andExpect(jsonPath("$.orders[1].tAmount", equalTo(500690)))
                .andExpect(jsonPath("$.orders[1].orderItemList", hasSize(3)))
                .andExpect(jsonPath("$.orders[1].createDate", notNullValue()))
                .andExpect(jsonPath("$.orders[1].lastModifiedDate", notNullValue()));
    }

    @Test
    public void getOrders_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1)
                .with(httpBasic("test1@gmail.com", "111"))
                .param("limit", "2")
                .param("offset", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.orders", hasSize(0)));
    }

    @Test
    public void getOrders_userHasNoOrder() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 2)
                .with(httpBasic("test1@gmail.com", "111"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.orders", hasSize(0)));
    }

    @Test
    public void getOrders_userNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 100)
                .with(httpBasic("test1@gmail.com", "111"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.orders", hasSize(0)));
    }

    /* Common Function */
    private RequestBuilder createOrder(
            CreateOrderRequest createOrderRequest,
            Integer userId,
            String username,
            String pwd) throws Exception
    {
        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", userId)
                .with(httpBasic(username, pwd))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(json);

        return requestBuilder;
    }


}