package com.ruby.mall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruby.mall.common.CheckResult;
import com.ruby.mall.dto.BuyItem;
import com.ruby.mall.dto.CreateOrderRequest;
import com.ruby.mall.dto.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@AutoConfigureMockMvc
@SpringBootTest
public class CsrfTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void register_admin_noCsrf() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("admin2@gmail.com");
        userRegisterRequest.setPwd("admin2");
        userRegisterRequest.setRoleName("ROLE_ADMIN");

        String json = objectMapper.writeValueAsString(userRegisterRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/admin/register")
                .with(httpBasic("admin@gmail.com", "admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        new CheckResult(mockMvc).check(requestBuilder, 403);
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
}
