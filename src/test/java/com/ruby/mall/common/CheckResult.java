package com.ruby.mall.common;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
    共用的 function
*/

public class CheckResult {
    private final MockMvc mockMvc;

    public CheckResult(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public void check(RequestBuilder requestBuilder, Integer code) throws Exception {
        /* 檢查回傳的 response code */
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(code));
    }

    public void check(RequestBuilder requestBuilder, Integer expectCode, String expectBody) throws Exception {
        /* 檢查回傳的 response code 和 response body*/
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectCode))
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertEquals(expectBody, body);
    }
}
