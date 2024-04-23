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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OrderFilter extends OncePerRequestFilter {
    /* 跟訂單相關的操作要檢查是否為本人 */
    private AuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
    private final UserDao userDao;

    public OrderFilter(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        String method = request.getMethod();
        if(url.matches("/users/\\d+/orders") && method.equals("POST")){
            Authentication authRequest = this.authenticationConverter.convert(request);
            String username = authRequest.getName();

            String[] parts = url.split("/");
            int userId = Integer.parseInt(parts[2]);

            com.ruby.mall.model.User member = userDao.getUserById(userId);
            if(member.getEmail().equals(username)){
                filterChain.doFilter(request, response);
            } else {
                StatusCode statusCode = StatusCode.AUTHENTICATION_ERROR_PERSON;
                response.setStatus(statusCode.getResponseCode());
                response.getWriter().write(statusCode.getResponseBody());
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
