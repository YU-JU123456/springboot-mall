package com.ruby.mall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//將前端傳來的註冊資料轉為 java obj
public class UserRegisterRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String pwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
