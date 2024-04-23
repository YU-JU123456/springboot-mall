package com.ruby.mall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruby.mall.constant.RoleCategory;
import com.ruby.mall.constant.StatusCode;
import com.ruby.mall.dao.UserDao;
import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 註冊新帳號
    @Test
    @Transactional
    public void register_user_success() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("user@gmail.com");
        userRegisterRequest.setPwd("123");

        RequestBuilder requestBuilder = registerRequestBuilder(userRegisterRequest);
        checkResult(requestBuilder, 201);
    }

    @Test
    public void check_hashPwdInDB(){
        // 檢查資料庫中的密碼不為明碼
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test1@gmail.com");
        userRegisterRequest.setPwd("111");

        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        assertNotEquals(userRegisterRequest.getPwd(), user.getPwd());
    }

    @Test
    public void register_invalidEmailFormat() throws Exception {
        // 錯誤的 email 格式
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("3gd8e7q34l9");
        userRegisterRequest.setPwd("123");

        RequestBuilder requestBuilder = registerRequestBuilder(userRegisterRequest);
        checkResult(requestBuilder, 400);
    }

    @Test
    public void register_emailAlreadyExist() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test1@gmail.com");
        userRegisterRequest.setPwd("111");

        StatusCode statusCode = StatusCode.AUTHENTICATION_ALREADY_EXIST;
        RequestBuilder requestBuilder = registerRequestBuilder(userRegisterRequest);
        checkResult(requestBuilder, statusCode.getResponseCode(), statusCode.getResponseBody());
    }


    @Test
    @Transactional
    public void register_admin_success() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("admin2@gmail.com");
        userRegisterRequest.setPwd("admin2");
        userRegisterRequest.setRole(RoleCategory.ROLE_ADMIN.getRoleIdx());

        RequestBuilder requestBuilder = registerAdminRequestBuilder(userRegisterRequest, "admin@gmail.com", "admin");
        checkResult(requestBuilder, 201, "註冊成功! user id 為: 5, 權限為: ROLE_ADMIN");
    }

    @Test
    public void register_admin_403() throws Exception {
        // 使用 user 權限註冊 admin 帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("admin2@gmail.com");
        userRegisterRequest.setPwd("admin2");
        userRegisterRequest.setRole(RoleCategory.ROLE_ADMIN.getRoleIdx());

        RequestBuilder requestBuilder = registerAdminRequestBuilder(userRegisterRequest, "test1@gmail.com", "111");
        checkResult(requestBuilder, 403);
    }

    @Test
    public void register_admin_401() throws Exception {
        // 使用錯誤帳密註冊 admin 帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("admin2@gmail.com");
        userRegisterRequest.setPwd("admin2");
        userRegisterRequest.setRole(RoleCategory.ROLE_ADMIN.getRoleIdx());

        RequestBuilder requestBuilder = registerAdminRequestBuilder(userRegisterRequest, "test1234@gmail.com", "111");
        checkResult(requestBuilder, 401);
    }

    // 登入
    @Test
    public void login_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .with(httpBasic("test1@gmail.com", "111"));

        checkResult(requestBuilder, 200, "test1@gmail.com, 登入成功! 權限為: [ROLE_USER]");
    }

    @Test
    public void login_wrongPassword() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .with(httpBasic("test1@gmail.com", "456"));

        checkResult(requestBuilder, 401);
    }

    @Test
    public void login_emailNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .with(httpBasic("test123@gmail.com", "111"));

        StatusCode statusCode = StatusCode.AUTHENTICATION_NOT_EXIST;
        checkResult(requestBuilder, statusCode.getResponseCode(), statusCode.getResponseBody());
    }

    /* Common Function */
    private void checkResult(RequestBuilder requestBuilder, Integer code) throws Exception {
        /* 檢查回傳的 response code */
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(code));
    }

    private void checkResult(RequestBuilder requestBuilder, Integer expectCode, String expectBody) throws Exception {
        /* 檢查回傳的 response code 和 response body*/
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectCode))
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertEquals(expectBody, body);
    }

    /* 使用 user 權限進行註冊 */
    private RequestBuilder registerRequestBuilder(UserRegisterRequest userRegisterRequest) throws Exception {
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        return requestBuilder;
    }

    /* 使用 admin 權限進行註冊 */
    private RequestBuilder registerAdminRequestBuilder(
            UserRegisterRequest userRegisterRequest,
            String username,
            String pwd
    ) throws Exception {
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/admin/register")
                .with(httpBasic(username, pwd))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        return requestBuilder;
    }
}