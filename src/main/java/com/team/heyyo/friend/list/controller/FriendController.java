package com.team.heyyo.friend.list.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.list.dto.FriendRequestDto;
import com.team.heyyo.friend.list.service.FriendService;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<TodoListMessageResponse> saveFriend(@AccessToken String accessToken , @RequestBody FriendRequestDto friendRequestDto) {
        TodoListMessageResponse result = friendService.saveFriend(accessToken , friendRequestDto.getToUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping
    public ResponseEntity deleteFriend(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.deleteFriend(friendRequestDto.getToUserId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity getFriendList(@AccessToken String accessToken) {
        List<UserResponse> result = friendService.getFriendList(accessToken);

        return ResponseEntity.ok().body(result);
    }

}
