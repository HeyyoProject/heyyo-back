package com.team.heyyo.user.controller;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserLoginResponse;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.handler.UserLoginHandler;
import com.team.heyyo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final UserLoginHandler loginHandler;

    @PostMapping("/register")
    public ResponseEntity<UserLoginResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(UserLoginResponse.of(200, "회원가입에 성공하였습니다."));
    }

    @GetMapping("/login")
    public ResponseEntity<UserLoginResponse> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserResponseCode responseCode = userService.isEmailAndPasswordCorrect(email, password);

        if (responseCode.name().equals("SUCCESS")) {
            setTokens(email, request, response);
        }

        return ResponseEntity.status(responseCode.getStatus())
                .body(UserLoginResponse.of(responseCode.getStatus(), responseCode.getMessage()));
    }

    private void setTokens(String email, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findByEmail(email);
        loginHandler.onAuthenticationSuccess(user, request, response);
    }

}
