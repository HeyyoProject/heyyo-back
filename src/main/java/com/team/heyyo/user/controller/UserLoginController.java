package com.team.heyyo.user.controller;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserLoginRequest;
import com.team.heyyo.user.dto.UserLoginResponse;
import com.team.heyyo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    if (responseCode.getStatus() == 200) {
      String nickname = userService.findByEmail(userLoginRequest.email()).getNickname();

      return ResponseEntity.status(responseCode.getStatus())
              .body(UserLoginResponse.of(responseCode.getMessage(), nickname));
    }

    return ResponseEntity.status(responseCode.getStatus())
            .body(UserBaseResponse.of(responseCode.getMessage()));
  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    userService.deleteRefreshTokenCookie(response);

    return ResponseEntity.ok().build();
  }
}
