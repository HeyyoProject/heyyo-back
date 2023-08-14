package com.team.heyyo.config;

import com.team.heyyo.auth.jwt.constant.JwtTokenConstant;
import com.team.heyyo.auth.jwt.repository.RefreshTokenRepository;
import com.team.heyyo.auth.jwt.service.RefreshTokenService;
import com.team.heyyo.auth.jwt.support.TokenAuthenticationFilter;
import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.auth.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.team.heyyo.auth.oauth.OAuth2SuccessHandler;
import com.team.heyyo.auth.oauth.Oauth2UserCustomService;
import com.team.heyyo.user.repository.UserRepository;
import com.team.heyyo.user.service.UserDetailService;
import com.team.heyyo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final Oauth2UserCustomService oauth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

//      토큰 재발급, 로그인 URL 은 열어두고, 나머지 API 는 인증 필요
        http.authorizeHttpRequests()
                .requestMatchers("/api/tokens", "/api/users/**").permitAll()
                .anyRequest().authenticated();

        http.oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(oauth2SuccessHandler())
                .userInfoEndpoint()
                .userService(oauth2UserCustomService);

        http.logout()
                .logoutUrl("/api/logout")
                .clearAuthentication(true)
                .deleteCookies(JwtTokenConstant.REFRESH_TOKEN.getName());

        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/**")  //인증되지 않는 사용자가 접근시 HTTP 401 리턴
                );

        return http.build();
    }

    @Bean
    UserDetailService customUserDetailsService(UserRepository userRepository) {
        return new UserDetailService(userRepository);
    }

    @Bean
    public OAuth2SuccessHandler oauth2SuccessHandler() {
        return new OAuth2SuccessHandler(
                tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, refreshTokenService, userService);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

}
