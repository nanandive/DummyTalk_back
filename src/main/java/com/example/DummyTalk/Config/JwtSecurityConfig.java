package com.example.DummyTalk.Config;

import com.example.DummyTalk.Jwt.JwtFilter;
import com.example.DummyTalk.Jwt.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider){

        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtFilter customeFilter = new JwtFilter(tokenProvider);                          // JwtFilter를 jwt 패키지에 추가
        http.addFilterBefore(customeFilter, UsernamePasswordAuthenticationFilter.class); // JwtFilter를 FilterChain 상에서 추가
    }
}