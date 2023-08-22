package com.team.heyyo.user.dto;

import lombok.Getter;

@Getter
public class UserLoginResponse extends UserBaseResponse{

    private final String nickname;

    private UserLoginResponse(String message, String nickname) {
        super(message);
        this.nickname = nickname;
    }

    public static UserLoginResponse of(String message, String nickname) {
        return new UserLoginResponse(message, nickname);
    }
}
