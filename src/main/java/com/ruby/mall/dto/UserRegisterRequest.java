package com.ruby.mall.dto;

import com.fasterxml.jackson.databind.JsonSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//將前端傳來的註冊資料轉為 java obj
public class UserRegisterRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String pwd;
    private String roleName = "ROLE_USER"; // 預設為一般權限

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
