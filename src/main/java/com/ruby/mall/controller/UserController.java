package com.ruby.mall.controller;

import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        return toRegisterService(userRegisterRequest);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> adminRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        return toRegisterService(userRegisterRequest);
    }

    @PostMapping("/users/login")
    public  ResponseEntity<String> login(Authentication authentication){
        // 取得使用者帳號
        String name = authentication.getName();
        // 取得使用者權限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return ResponseEntity.status(HttpStatus.OK).body(name + ", 登入成功! " + "權限為: " + authorities);
    }

    private ResponseEntity<String> toRegisterService(UserRegisterRequest userRegisterRequest){
        try{
            String msg = userService.register(userRegisterRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("註冊成功! " + msg);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}