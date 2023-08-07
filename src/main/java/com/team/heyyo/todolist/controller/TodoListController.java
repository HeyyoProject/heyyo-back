package com.team.heyyo.todolist.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.todolist.domain.TodoList;
import com.team.heyyo.todolist.dto.TodoListDataResponse;
import com.team.heyyo.todolist.dto.TodoListDateRequest;
import com.team.heyyo.todolist.dto.TodoListDataRequest;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.todolist.service.TodoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
public class TodoListController {

    private final TodoListService todoListService;

    @PostMapping
    public ResponseEntity<TodoListMessageResponse> saveTodoList(@AccessToken String accessToken , @RequestBody TodoListDataRequest todoListDataRequest) {
        TodoListMessageResponse result = todoListService.saveTodoList(accessToken , todoListDataRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{todoListId}")
    public ResponseEntity deleteTodoList(@PathVariable long todoListId) {
        todoListService.deleteTodoList(todoListId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{todoListId}")
    public ResponseEntity<TodoListMessageResponse> updateTodoList(@AccessToken String accessToken
            , @PathVariable long todoListId , @RequestBody TodoListDataRequest todoListDataRequest) {
        TodoListMessageResponse result = todoListService.updateTodoList(accessToken , todoListId , todoListDataRequest);

        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{todoListId}/complete")
    public ResponseEntity<TodoListMessageResponse> updateTodoListComplete(@AccessToken String accessToken, @PathVariable long todoListId) {
        TodoListMessageResponse result = todoListService.updateTodoListComplete(accessToken , todoListId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/progress")
    public ResponseEntity<TodoListDataResponse> findTodoListInProgressByUserId(@AccessToken String accessToken) {
        TodoListDataResponse<List<TodoList>> result = todoListService.findTodoListInProgressByUserId(accessToken);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/achieve")
    public ResponseEntity<TodoListDataResponse> findAchievedTodoListByUserId(@AccessToken String accessToken) {
        TodoListDataResponse<List<TodoList>> result = todoListService.findAchievedTodoListByUserId(accessToken);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/achieve/date")
    public ResponseEntity<TodoListDataResponse> getTodoListByDateAchieved(@AccessToken String accessToken , @RequestBody TodoListDateRequest todoListDateRequest) {
        TodoListDataResponse<List<TodoList>> result = todoListService.getTodoListByDateAchieved(accessToken , todoListDateRequest);

        return ResponseEntity.ok().body(result);
    }

}
