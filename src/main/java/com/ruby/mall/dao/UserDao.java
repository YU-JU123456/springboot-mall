package com.ruby.mall.dao;

import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.Role;
import com.ruby.mall.model.User;

import java.util.List;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    Integer createUserRole(UserRegisterRequest userRegisterRequest, Integer userId);
    Role getRoleNameByURoleId(Integer uRoleId); // 由 UserRoleId 取得 roleName
    List<Role> getRolesByUserId(Integer userId);
}
