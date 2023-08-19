package com.team.heyyo.user.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.user.dto.UserModifyRequest;
import com.team.heyyo.user.dto.UserResponse;
import com.team.heyyo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserPreferenceController {

    private final UserService userService;

    @PatchMapping("/password")
    public ResponseEntity<TodoListMessageResponse> modifyPasswordByToken(@AccessToken String accessToken , @RequestBody UserModifyRequest userRequest) throws AccountException {
        TodoListMessageResponse result = userService.updatePasswordByToken(accessToken , userRequest);

        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<TodoListMessageResponse> modifyNicknameByToken(@AccessToken String accessToken , @RequestBody UserModifyRequest userRequest) throws AccountException {
        TodoListMessageResponse result = userService.updateNickNameByToken(accessToken , userRequest);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserByToken(@AccessToken String accessToken) throws AccountException {
        UserResponse userResponse = userService.getUserByToken(accessToken);

        return ResponseEntity.ok().body(userResponse);
    }

}
