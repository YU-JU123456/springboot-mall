package com.ruby.mall.constant;

import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

public enum StatusCode {
    /* Code */
    AUTHENTICATION_NOT_EXIST(40101, "Account not exist"), // login: 帳號不存在
    AUTHENTICATION_ALREADY_EXIST(40001, "Account already exist"), // register: 帳號已存在
    AUTHENTICATION_ROLE_ILLEGAL(40002, "RoleId illegle"); // register: roleId 不存在

    /* Getter & Setter */
    private Integer statusCode;
    private String msg;

    StatusCode(Integer statusCode, String status) {
        this.statusCode = statusCode;
        this.msg = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("code", statusCode);
        body.put("msg", msg);
        return body.toString();
    }
}
