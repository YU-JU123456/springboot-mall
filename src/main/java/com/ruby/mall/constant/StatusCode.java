package com.ruby.mall.constant;

import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

public enum StatusCode {
    /* Code */
    AUTHENTICATION_ALREADY_EXIST(400,40001, "Account already exist"), // register: 帳號已存在
    AUTHENTICATION_ROLE_ILLEGAL(400,40002, "RoleId illegle"), // register: roleId 不存在

    AUTHENTICATION_NOT_EXIST(401, 40101, "Account not exist"), // login: 帳號不存在
    AUTHENTICATION_ERROR_PERSON(401, 40102, "No authorization from the individual"); // order: 不是本人

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
