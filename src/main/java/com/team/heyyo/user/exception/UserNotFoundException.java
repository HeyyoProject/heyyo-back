package com.team.heyyo.user.exception;

import com.team.heyyo.advice.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final String MESSAGE = "멤버가 존재하지 않습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
