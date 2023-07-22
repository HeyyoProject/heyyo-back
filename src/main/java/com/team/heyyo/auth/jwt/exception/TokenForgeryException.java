package com.team.heyyo.auth.jwt.exception;

public class TokenForgeryException extends RuntimeException {
    public TokenForgeryException(String message) {
        super(message);
    }
}