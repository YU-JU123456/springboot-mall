package com.ruby.mall.service.impl;

import com.ruby.mall.constant.RoleCategory;
import exception.AuthenticationAlreadyExist;
import com.ruby.mall.dao.UserDao;
import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.Role;
import com.ruby.mall.model.User;
import com.ruby.mall.service.UserService;
import exception.AuthenticationRoleillegle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class); // 要選 slf4j 的 logger

    @Override
    @Transactional
    public String register(UserRegisterRequest userRegisterRequest) throws AuthenticationAlreadyExist, AuthenticationRoleillegle{
        // 檢查註冊的 email
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if(user != null){
            log.warn("email {} 已經被註冊", userRegisterRequest.getEmail());
            throw new AuthenticationAlreadyExist();
        }

        // 檢查權限合法性
        try {
            RoleCategory roleCategory = RoleCategory.getRoleByValue(userRegisterRequest.getRole());
        } catch (IllegalArgumentException e){
            throw new AuthenticationRoleillegle();
        }

        // Hash 原始密碼
        String pwd = passwordEncoder.encode(userRegisterRequest.getPwd());
        userRegisterRequest.setPwd(pwd);

        // 創建帳號
        Integer usrId = userDao.createUser(userRegisterRequest);
        User usr = userDao.getUserById(usrId);

        // 創建 user & role 關聯表
        Integer usrRoleIdx = userDao.createUserRole(userRegisterRequest, usrId);
        Role role = userDao.getRoleNameByURoleId(usrRoleIdx);

        // output
        StringBuilder msg = new StringBuilder();
        msg.append("user id 為: ");
        msg.append(usr.getUserId());
        msg.append(", 權限為: ");
        msg.append(role.getRoleName());
        return msg.toString();
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
