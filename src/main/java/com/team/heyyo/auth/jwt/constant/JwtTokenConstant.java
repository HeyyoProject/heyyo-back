package com.team.heyyo.auth.jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum JwtTokenConstant {
    ACCESS_TOKEN(Duration.ofMinutes(30), "Authorization"),
    REFRESH_TOKEN(Duration.ofDays(14), "refresh_token"),
    ;

    private final Duration duration;
    private final String name;

}
