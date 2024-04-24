package com.ruby.mall.security;

import com.ruby.mall.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity()
public class SecurityConfig {

    @Autowired
    private UserDao userDao;

    @Bean
    public PasswordEncoder passwordEncoder (){
        // 指定要使用哪一種演算法, 實作密碼加密
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // 以下所有方法都要選擇沒有畫刪除線的
        return http
                // 設定 session 的創建機制
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                .cors( cors -> cors
                        .configurationSource(createCorsConfig())
                )
                // 設定 CSRF 的保護
                .csrf(csrf -> csrf
                        // 要在 cookie 裡回傳 csrf token 給前端
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                        // 檢查 request header 裡有沒有 xsrf token
                        .csrfTokenRequestHandler(createCsrfHandler())
                        .ignoringRequestMatchers("/users/register", "/users/login")
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/register").permitAll() // 所有人都可以使用
                        .requestMatchers("/admin/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/products").authenticated()
                        .requestMatchers("/products/**").authenticated()
                        .anyRequest().denyAll()
                )
                .addFilterBefore(new LoginFilter(userDao), BasicAuthenticationFilter.class)
                .addFilterBefore(new OrderFilter(userDao), BasicAuthenticationFilter.class)
                .build();
    }

    private CsrfTokenRequestAttributeHandler createCsrfHandler(){
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);
        return csrfHandler;
    }

    private CorsConfigurationSource createCorsConfig(){
        CorsConfiguration configuration = new CorsConfiguration();
        // 表示後端允許的請求來源有哪些, 當 setAllowCredentials 為 true 則不能為 *
        configuration.setAllowedOrigins(List.of("http://www.ruby.com"));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));

        configuration.setAllowCredentials(true); // 後端是否允許前端帶上 cookie
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
