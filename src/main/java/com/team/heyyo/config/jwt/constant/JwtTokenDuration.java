package com.team.heyyo.config.jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum JwtTokenDuration {
    ACCESS_TOKEN_EXPIRED(Duration.ofMinutes(30)),
    REFRESH_TOKEN_EXPIRED(Duration.ofDays(14)),
    ;

    private final Duration duration;

}
