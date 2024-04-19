package com.ruby.mall.service;

import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
