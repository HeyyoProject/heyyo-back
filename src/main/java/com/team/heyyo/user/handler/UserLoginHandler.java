package com.team.heyyo.user.handler;

import com.team.heyyo.auth.jwt.domain.RefreshToken;
import com.team.heyyo.auth.jwt.repository.RefreshTokenRepository;
import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.team.heyyo.auth.jwt.constant.JwtTokenConstant.ACCESS_TOKEN;
import static com.team.heyyo.auth.jwt.constant.JwtTokenConstant.REFRESH_TOKEN;

@RequiredArgsConstructor
@Component
public class UserLoginHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void onAuthenticationSuccess(User user, HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN.getDuration());
        saveRefreshToken(user.getUserId() , refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN.getDuration());
        response.setHeader("Authorization", "Bearer "+ accessToken);

    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN.getDuration().toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getName());
        CookieUtil.addCookie(response, REFRESH_TOKEN.getName(), refreshToken, cookieMaxAge);
    }


}
