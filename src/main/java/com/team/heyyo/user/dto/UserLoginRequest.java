package com.team.heyyo.user.dto;

public record UserLoginRequest(
        String email,
        String password
) {
    public static UserLoginRequest of (String email, String password) {
        return new UserLoginRequest(email, password);
    }
}
