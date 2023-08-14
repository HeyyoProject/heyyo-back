package com.team.heyyo.todolist.repository;

import com.team.heyyo.todolist.domain.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoListRepository extends JpaRepository<TodoList, Long> , CustomTodoListRepository {

    Optional<TodoList> findTodoListByUserId(long userId);

    void deleteTodoListByTodoListId(long todoListId);

}
