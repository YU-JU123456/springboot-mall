package com.ruby.mall.service;

import exception.AuthenticationAlreadyExist;
import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.User;
import exception.AuthenticationRoleillegle;

public interface UserService {
    String register(UserRegisterRequest userRegisterRequest) throws AuthenticationAlreadyExist, AuthenticationRoleillegle;
    User getUserById(Integer userId);
}
