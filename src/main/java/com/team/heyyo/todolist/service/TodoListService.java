package com.team.heyyo.todolist.service;

import com.team.heyyo.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    public void addTodoList(String accessToken) {

    }

}
