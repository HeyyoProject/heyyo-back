package com.team.heyyo.user.controller;

import static org.springframework.http.HttpStatus.*;

import com.team.heyyo.mail.dto.MailMessage;
import com.team.heyyo.mail.service.MailService;
import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserFindController {

  private final UserService userService;
  private final MailService mailService;

  @GetMapping("/passwords/{name}/{email}")
  public ResponseEntity<UserBaseResponse> findPassword(
      @PathVariable String name,
      @PathVariable String email
  ) {
    UserResponseCode responseCode = userService.findPasswordWithEmailAndName(email, name);

    if (responseCode.equals(UserResponseCode.SUCCESS)) {
      mailService.sendTemporaryPassword(MailMessage.of(email));
      return ResponseEntity.ok(
          UserBaseResponse.of("임시 비밀번호가 발급되었습니다.\n 임시 비밀번호로 로그인 후 비밀번호를 변경해주세요."));
    } else {
      return ResponseEntity.status(NOT_FOUND)
          .body(UserBaseResponse.of("입력하신 정보와 일치하는 회원이 없습니다."));
    }
  }
}
