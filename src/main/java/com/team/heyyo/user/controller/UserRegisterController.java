package com.team.heyyo.user.controller;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserRegisterController {

  private final UserService userService;

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
}

