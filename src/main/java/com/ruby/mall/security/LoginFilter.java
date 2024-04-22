package com.ruby.mall.security;

import com.ruby.mall.constant.StatusCode;
import com.ruby.mall.dao.UserDao;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoginFilter extends OncePerRequestFilter {


    private AuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
    private final UserDao userDao;

    public LoginFilter(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * login 時檢查 account 是否存在
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if(url.equals("/users/login")){
            Authentication authRequest = this.authenticationConverter.convert(request);
            String username = authRequest.getName();

            com.ruby.mall.model.User member = userDao.getUserByEmail(username);
            if(member == null){
                String body = StatusCode.AUTHENTICATION_NOT_EXIST.getResponseBody();
                response.setStatus(401);
                response.getWriter().write(body);
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}