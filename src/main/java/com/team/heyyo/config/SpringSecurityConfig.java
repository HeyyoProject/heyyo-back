package com.team.heyyo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * TODO. JWT, OAUTH2 구현 필요
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .httpBasic().disable()
            // 세션을 사용하지 않기 때문에 STATELESS로 설정
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // H2 콘솔 사용을 위한 설정
            .and().headers(headers -> headers.frameOptions().sameOrigin());

        http
            .authorizeHttpRequests()
            .requestMatchers("/login/**").permitAll()
            .requestMatchers("/signup/**").permitAll()
            .requestMatchers(PathRequest.toH2Console()).permitAll()// h2-console
            .anyRequest().authenticated(); // 그 외 인증 없이 접근X

        return http.build();
    }

    /* 비밀번호 암호화 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
