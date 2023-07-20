package com.team.heyyo.config.jwt.service;

import com.team.heyyo.config.jwt.dto.AccessTokenRequest;
import com.team.heyyo.config.jwt.dto.AccessTokenResponse;
import com.team.heyyo.config.jwt.support.TokenProvider;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    private final Duration REFRESH_TOKEN_EXPIRED_TWO_HOURS = Duration.ofHours(2);

    /**
     * refreshToken으로 토큰이 유효한지 검사
     * 유효하면 db에서 refreshToken으로 사용자 id를 찾고
     * 찾은 userId 로 accessToken을 만들어주고 반환해준다
     * @param request : refreshToken
     * @return new AccessToken
     */
    public AccessTokenResponse createNewAccessToken(AccessTokenRequest request) {
//        토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(request.refreshToken())) {
            throw new IllegalArgumentException("Unexpected Token");
        }

        Long userId = refreshTokenService.findByRefreshToken(request.refreshToken()).getUserId();
        User user = userService.findById(userId);

        String newAccessToken = tokenProvider.generateToken(user, REFRESH_TOKEN_EXPIRED_TWO_HOURS);
        return new AccessTokenResponse(newAccessToken);
    }
}
