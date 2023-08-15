package com.team.heyyo.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserResponseCode {
  SUCCESS(200, "성공"),
  ID_NOT_FOUND(404, "id와 일치하는 회원이 없습니다."),
  PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),
  EMAIL_NOT_FOUND(404, "해당 이메일과 일치하는 회원이 없습니다."),
  EMAIL_DUPLICATION(400, "이미 존재하는 이메일입니다."),
  NICKNAME_DUPLICATION(400, "사용 불가능한 닉네임입니다."),
  NAME_PASSWORD_NOT_MATCH(404, "이름과 비밀번호와 일치하는 회원이 없습니다.");

  private final int status;
  private final String message;
}
