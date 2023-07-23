package com.team.heyyo.auth.exception;

import com.team.heyyo.advice.ForbiddenException;

public class AuthorizationException extends ForbiddenException {

    private static final String MESSAGE = "권한이 없습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}