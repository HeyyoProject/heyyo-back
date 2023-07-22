package com.team.heyyo.auth.jwt.service;

import com.team.heyyo.auth.jwt.domain.RefreshToken;
import com.team.heyyo.auth.jwt.exception.TokenForgeryException;
import com.team.heyyo.auth.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenForgeryException("변조되거나, 알 수 없는 refreshToken 입니다."));
    }

}
