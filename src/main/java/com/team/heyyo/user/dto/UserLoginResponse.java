package com.team.heyyo.user.dto;

public record UserLoginResponse(
        int code,
        String message
) {
    public static UserLoginResponse of(int code, String message) {
        return new UserLoginResponse(code, message);
    }
}

