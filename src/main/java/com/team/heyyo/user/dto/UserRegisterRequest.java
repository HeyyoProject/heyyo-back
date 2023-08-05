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
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    private String recommendEmail;

    private UserRegisterRequest(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static UserRegisterRequest of(String name, String email, String password, String phoneNumber) {
        return new UserRegisterRequest(name, email, password, phoneNumber);
    }
    public static UserRegisterRequest of(String name, String email, String password, String phoneNumber, String recommendEmail) {
        return new UserRegisterRequest(name, email, password, phoneNumber, recommendEmail);
    }

}