package com.team.heyyo.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserResponseCode {
    SUCCESS(200, "성공"),
    ID_NOT_FOUND(400, "id와 일치하는 user가 없습니다."),
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(400, "해당 email과 일치하는 user가 없습니다."),
    EMAIL_DUPLICATION(400, "이미 존재하는 email입니다."),
    NAME_DUPLICATION(400, "이미 존재하는 name입니다.");

    private final int status;
    private final String message;
}
