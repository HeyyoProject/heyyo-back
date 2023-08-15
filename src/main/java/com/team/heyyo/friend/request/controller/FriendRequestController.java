package com.team.heyyo.friend.request.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.request.dto.FriendRequestDto;
import com.team.heyyo.friend.request.service.FriendRequestService;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friend-request")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<TodoListMessageResponse> saveFriendRequest(@AccessToken String accessToken , @RequestBody FriendRequestDto friendRequestDto) {
        TodoListMessageResponse result = friendRequestService.saveFriendRequest(accessToken , friendRequestDto.getToUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity deleteFriendRequest(@PathVariable long requestId) {
        friendRequestService.deleteFriendRequest(requestId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getFriendRequestList(@AccessToken String accessToken) {
        List<UserResponse> result = friendRequestService.getFriendRequestList(accessToken);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<TodoListMessageResponse> approveTheFriendRequest(@PathVariable long requestId) {
        TodoListMessageResponse result = friendRequestService.approveTheFriendRequest(requestId);

        return ResponseEntity.ok().body(result);
    }

}
