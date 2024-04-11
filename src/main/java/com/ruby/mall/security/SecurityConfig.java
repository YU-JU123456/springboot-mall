package com.ruby.mall.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder (){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // 以下所有方法都要選擇沒有畫刪除線的
        return http
                .csrf(csrf -> csrf.disable()) //
                .httpBasic(Customizer.withDefaults()) //
                .formLogin(Customizer.withDefaults()) //

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/register", "/users/login").permitAll() // 所有人都可以使用
                        .anyRequest().authenticated() // 所有 request 都要登入才可以請求
                )
                .build();
    }
}
