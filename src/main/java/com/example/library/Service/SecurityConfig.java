package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers(
                    "/api/auth/**",      // mở truy cập cho đăng ký, đăng nhập
                    "/",                 // mở trang chủ
                    "/actuator/**",      // mở cho Prometheus hoặc health check
                    "/swagger-ui/**",    // mở tài liệu Swagger UI
                    "/v3/api-docs/**"    // mở OpenAPI docs
                ).permitAll()
                .anyRequest().authenticated() // các endpoint khác yêu cầu xác thực
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
