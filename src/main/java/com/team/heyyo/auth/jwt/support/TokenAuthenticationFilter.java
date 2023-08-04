package com.team.heyyo.auth.jwt.support;

import com.team.heyyo.auth.jwt.service.RefreshTokenService;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.team.heyyo.auth.jwt.constant.JwtTokenConstant.ACCESS_TOKEN;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractAccessToken(request);

        if (tokenProvider.validToken(token)) {
            setAuthenticationInSecurityContextHolder(token);
        } else {
            Cookie refreshTokenCookie = findCookie(request, REFRESH_TOKEN_COOKIE_NAME);

            if (refreshTokenCookie != null && tokenProvider.validToken(refreshTokenCookie.getValue())) {
                String accessToken = generateNewAccessToken(response, refreshTokenCookie.getValue());
                setAuthenticationInSecurityContextHolder(accessToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);
        return token;
    }

    private void setAuthenticationInSecurityContextHolder(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private String generateNewAccessToken(HttpServletResponse response, String refreshToken) {
        User user = findUserWithRefreshToken(refreshToken);
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN.getDuration());
        response.setHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);
        return accessToken;
    }

    private User findUserWithRefreshToken(String refreshToken) {
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);
        return user;
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
