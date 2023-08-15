package com.team.heyyo.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRegisterRequest {

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    private String recommendNickname;

    private UserRegisterRequest(String name, String nickname, String email, String password, String phoneNumber) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static UserRegisterRequest of(String name, String nickname, String email, String password, String phoneNumber) {
        return new UserRegisterRequest(name, nickname,email, password, phoneNumber);
    }
    public static UserRegisterRequest of(String name, String nickname, String email, String password, String phoneNumber, String recommendNickname) {
        return new UserRegisterRequest(name ,nickname, email, password, phoneNumber, recommendNickname);
    }

}