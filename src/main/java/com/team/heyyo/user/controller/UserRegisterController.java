package com.team.heyyo.user.controller;

import com.team.heyyo.sms.dto.SmsResponseDto;
import com.team.heyyo.sms.service.SmsService;
import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserRegisterController {

  private final UserService userService;
  private final SmsService smsService;

  @PostMapping("/register")
  public ResponseEntity<UserBaseResponse> register(
      @RequestBody @Valid UserRegisterRequest request) {
    userService.register(request);
    return ResponseEntity.ok(UserBaseResponse.of("회원가입에 성공하였습니다."));
  }

  @GetMapping("/duplicate/emails/{email}")
  public ResponseEntity<UserBaseResponse> isEmailDuplicate(@PathVariable String email) {
    UserResponseCode responseCode = userService.isEmailDuplicate(email);

    return ResponseEntity.status(responseCode.getStatus())
        .body(UserBaseResponse.of(responseCode.getMessage()));
  }

  @GetMapping("/duplicate/nicknames/{nickname}")
  public ResponseEntity<UserBaseResponse> isNicknameDuplicate(@PathVariable String nickname) {
    UserResponseCode responseCode = userService.isNicknameDuplicate(nickname);

    return ResponseEntity.status(responseCode.getStatus())
            .body(UserBaseResponse.of(responseCode.getMessage()));
  }

  @GetMapping("/sms/{to}")
  public ResponseEntity<SmsResponseDto> sendSmsTo(@PathVariable String to) {
    SmsResponseDto smsResponseDto = smsService.sendSmsToUserWithCertificationNumber(to);

    return ResponseEntity.status(Integer.parseInt(smsResponseDto.getStatusCode())).body(smsResponseDto);
  }
}

