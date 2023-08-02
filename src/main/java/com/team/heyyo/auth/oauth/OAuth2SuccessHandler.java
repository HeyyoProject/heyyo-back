package com.team.heyyo.auth.oauth;

import com.team.heyyo.auth.jwt.domain.RefreshToken;
import com.team.heyyo.auth.jwt.repository.RefreshTokenRepository;
import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.service.UserLoginService;
import com.team.heyyo.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.team.heyyo.auth.jwt.constant.JwtTokenConstant.ACCESS_TOKEN;
import static com.team.heyyo.auth.jwt.constant.JwtTokenConstant.REFRESH_TOKEN;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * FIXME : REDIRECT_PATH 수정 필요
     */

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserLoginService userService;

    @Value("${frontend.url}")
    private String CLIENT_URL;
    private String REDIRECT_PATH = "/main";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN.getDuration());
        saveRefreshToken(user.getUserId() , refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN.getDuration());
        response.setHeader(ACCESS_TOKEN.getName(), "Bearer "+ accessToken);

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, getTargetUrl());
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

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl() {
        return UriComponentsBuilder.fromUriString(CLIENT_URL + REDIRECT_PATH)
                .build()
                .toUriString();
    }
}