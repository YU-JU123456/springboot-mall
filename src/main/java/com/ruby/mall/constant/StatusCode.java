package com.ruby.mall.constant;

public enum StatusCode {
    /* Code */
    AUTHENTICATION_410(410, "AUTHENTICATION_NOT_EXIST");

    /* Getter & Setter */
    private Integer statusCode;
    private String status;

    StatusCode(Integer statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }
}
