package com.team.heyyo.user.controller;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.dto.UserLoginRequest;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserLoginController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserBaseResponse> login(
      @RequestBody UserLoginRequest userLoginRequest,
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    UserResponseCode responseCode = userService.isEmailAndPasswordCorrect(userLoginRequest.email(),
        userLoginRequest.password());
    userService.setTokensIfEmailAndPasswordCorrect(responseCode, userLoginRequest.email(), request,
        response);

    return ResponseEntity.status(responseCode.getStatus())
        .body(UserBaseResponse.of(responseCode.getMessage()));
  }
}
