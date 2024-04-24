package com.ruby.mall.service;

import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.exception.MallException;
import com.ruby.mall.model.User;

public interface UserService {
    String register(UserRegisterRequest userRegisterRequest) throws MallException;
    User getUserById(Integer userId);
}
