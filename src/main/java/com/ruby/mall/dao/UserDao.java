package com.ruby.mall.dao;

import com.ruby.mall.dto.UserLoginRequest;
import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User getUserByEmail(String email);
}
