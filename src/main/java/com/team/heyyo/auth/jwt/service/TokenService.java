package com.team.heyyo.auth.jwt.service;

import com.team.heyyo.auth.jwt.dto.AccessTokenResponse;
import com.team.heyyo.auth.jwt.exception.TokenForgeryException;
import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.auth.jwt.dto.AccessTokenRequest;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.team.heyyo.auth.jwt.constant.JwtTokenConstant.ACCESS_TOKEN;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


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
            throw new TokenForgeryException("변조되거나, 만료된 accessToken 입니다.");
        }

        Long userId = refreshTokenService.findByRefreshToken(request.refreshToken()).getUserId();
        User user = userService.findById(userId);

        String newAccessToken = tokenProvider.generateToken(user, ACCESS_TOKEN.getDuration());
        return new AccessTokenResponse(newAccessToken);
    }
}
