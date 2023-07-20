package com.team.heyyo.config.jwt.support;

import com.team.heyyo.config.jwt.JwtFactory;
import com.team.heyyo.config.jwt.constant.JwtProperties;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        //given
        final Long userId = 1L;
        final String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build().createToken(jwtProperties);

        //when
        Long userIdByToken = tokenProvider.getUserId(token);

        //then
        assertThat(userIdByToken).isEqualTo(userId);

    }

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        //given
        User testUser = userRepository.save(
                User.builder()
                .email("user@example.com")
                .password("test")
                .build()
        );

        //when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        //then
        Long userId = tokenProvider.getUserId(token);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        //given
        String expiredAccessToken = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(1).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when
        boolean isValid = tokenProvider.validToken(expiredAccessToken);

        //then
        assertThat(isValid).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰이면 유효성 검증에 성공")
    @Test
    void validToken_validToken() {
        //given
        String expiredAccessToken = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() + Duration.ofDays(1).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when
        boolean isValid = tokenProvider.validToken(expiredAccessToken);

        //then
        assertThat(isValid).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        //given
        final String userEmail = "user@email.com";
        final String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        //when
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);

    }
}