package com.ruby.mall.dao;

import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.Role;
import com.ruby.mall.model.User;

import java.util.List;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    Integer createUserRole(Integer userId, Integer roleId);
    Role getRoleByRoleName(String roleName);
    List<Role> getRolesByUserId(Integer userId);
}
