package com.ruby.mall.security;

import com.ruby.mall.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    /**
     * 用途:根據使用者輸入的帳號, 查詢會員數據, 並回傳 security 要的 User 格式
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* 從資料庫中查詢 member 數據 */
        com.ruby.mall.model.User member = userDao.getUserByEmail(username);

        String memberEmail = member.getEmail();
        String memberPwd = member.getPwd();

        // 跟權限有關的設定
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 轉換成 Spring Security 指定的 User 格式
        return new User(memberEmail, memberPwd, authorities);
    }
}
