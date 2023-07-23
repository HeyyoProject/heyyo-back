package com.team.heyyo.auth.exception;

public class LoginFailedException extends RuntimeException {

    private static final String MESSAGE = "아이디나 비밀번호가 잘못되었습니다";

    public LoginFailedException() {
        super(MESSAGE);
    }
}
