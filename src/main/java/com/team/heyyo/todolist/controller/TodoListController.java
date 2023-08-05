package com.team.heyyo.todolist.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.todolist.dto.TodoListDateRequest;
import com.team.heyyo.todolist.dto.TodoListRequest;
import com.team.heyyo.todolist.service.TodoListService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoListController {

    private final TodoListService todoListService;

    @PostMapping
    public ResponseEntity saveTodoList(@AccessToken String accessToken , @RequestBody TodoListRequest todoListRequest) {
        return ResponseEntity.ok().body(todoListService.saveTodoList(accessToken , todoListRequest));
    }

    @DeleteMapping("/{todoListId}")
    public ResponseEntity deleteTodoList(@PathVariable long todoListId) {
        todoListService.deleteTodoList(todoListId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{todoListId}")
    public ResponseEntity updateTodoList(@AccessToken String accessToken
            , @PathVariable long todoListId , @RequestBody TodoListRequest todoListRequest) {
        return ResponseEntity.ok().body(todoListService.updateTodoList(accessToken , todoListId , todoListRequest));
    }

    @PatchMapping("/{todoListId}/complete")
    public ResponseEntity updateTodoListComplete(@AccessToken String accessToken, @PathVariable long todoListId) {
        return ResponseEntity.ok().body(todoListService.updateTodoListComplete(accessToken , todoListId));
    }

    @GetMapping("/progress")
    public ResponseEntity findTodoListInProgressByUserId(@AccessToken String accessToken) {
        return ResponseEntity.ok().body(todoListService.findTodoListInProgressByUserId(accessToken));
    }

    @GetMapping("/achieve")
    public ResponseEntity findAchievedTodoListByUserId(@AccessToken String accessToken) {
        return ResponseEntity.ok().body(todoListService.findAchievedTodoListByUserId(accessToken));
    }

    @GetMapping("/achieve/date")
    public ResponseEntity getTodoListByDateAchieved(@AccessToken String accessToken , @RequestBody TodoListDateRequest todoListDateRequest) {
        return ResponseEntity.ok().body(todoListService.getTodoListByDateAchieved(accessToken , todoListDateRequest));
    }

}
