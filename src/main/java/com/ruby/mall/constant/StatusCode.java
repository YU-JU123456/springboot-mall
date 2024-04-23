package com.ruby.mall.constant;

import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

public enum StatusCode {
    /* Code */
    AUTHENTICATION_ALREADY_EXIST(400,4000101, "Account lready exist"), // register: 帳號已存在
    AUTHENTICATION_ROLE_ILLEGAL(400,4000102, "RoleId illegle"), // register: roleId 不存在
    AUTHENTICATION_NOT_EXIST(401, 4010101, "Account not exist"), // login: 帳號不存在
    AUTHENTICATION_ERROR_PERSON(401, 4010102, "No authorization from the individual"), // order: 不是本人

    ORDER_USER_NOT_EXIST(400, 4000201, "User not exist"),
    ORDER_PRODUCT_NOT_EXIST(400, 4000202, "Product not exist"),
    ORDER_STOCK_LIMITED(400, 4000203, "Stock insufficient")
    ;

    /* Getter & Setter */
    private Integer responseCode;
    private Integer errorCode;
    private String msg;

    StatusCode(Integer responseCode, Integer errorCode, String status) {
        this.responseCode = responseCode;
        this.errorCode = errorCode;
        this.msg = status;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("code", errorCode);
        body.put("msg", msg);
        return body.toString();
    }
}
