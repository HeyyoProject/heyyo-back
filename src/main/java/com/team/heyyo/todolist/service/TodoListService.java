package com.team.heyyo.todolist.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.todolist.domain.TodoList;
import com.team.heyyo.todolist.dto.TodoListDataResponse;
import com.team.heyyo.todolist.dto.TodoListDateRequest;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.todolist.dto.TodoListDataRequest;
import com.team.heyyo.todolist.exception.TodoListException;
import com.team.heyyo.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public TodoListMessageResponse saveTodoList(String accessToken , TodoListDataRequest todoListDataRequest) {
        long userId = tokenProvider.getUserId(accessToken);

        TodoList todoList = TodoList.of(todoListDataRequest.getData() , userId);
        todoListRepository.save(todoList);

        return TodoListMessageResponse.of("성공적으로 생성되었습니다.");
    }

    @Transactional
    public void deleteTodoList(long todoListId) {
        todoListRepository.deleteTodoListByTodoListId(todoListId);
    }

    @Transactional
    public TodoListMessageResponse updateTodoList(String accessToken , long todoListId , TodoListDataRequest todoListDataRequest) {
        TodoList todoList = findAndCheckValid(accessToken , todoListId);

        todoList.updateData(todoListDataRequest.getData());

        return TodoListMessageResponse.of("성공적으로 수정되었습니다.");
    }

    @Transactional
    public TodoListMessageResponse updateTodoListComplete(String accessToken , long todoListId) {
        TodoList todoList = findAndCheckValid(accessToken , todoListId);

        todoList.updateIsComplete(true);

        return TodoListMessageResponse.of("성공적으로 수정되었습니다.");
    }

    public TodoList findAndCheckValid(String accessToken , long todoListId) {
        long userId = tokenProvider.getUserId(accessToken);

        TodoList todoList = todoListRepository.findById(todoListId)
                .orElseThrow(() -> new TodoListException("해당 데이터를 찾을 수 없습니다."));

        if(todoList.getUserId() != userId) {
            throw new TodoListException("비정상적인 접근입니다.");
        }

        return todoList;
    }

    public TodoListDataResponse findTodoListInProgressByUserId(String accessToken) {
        long userId = tokenProvider.getUserId(accessToken);

        return TodoListDataResponse.of(todoListRepository.findTodoListInProgressByUserId(userId));
    }

    public TodoListDataResponse findAchievedTodoListByUserId(String accessToken) {
        long userId = tokenProvider.getUserId(accessToken);

        return TodoListDataResponse.of(todoListRepository.findAchievedTodoListByUserId(userId));
    }

    public TodoListDataResponse getTodoListByDateAchieved(String accessToken , TodoListDateRequest todoListDateRequest) {
        long userId = tokenProvider.getUserId(accessToken);

        return TodoListDataResponse.of(todoListRepository.getTodoListByDateAchieved(userId , todoListDateRequest.getDate()));
    }

}
