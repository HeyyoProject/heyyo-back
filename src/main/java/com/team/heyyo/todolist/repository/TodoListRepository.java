package com.team.heyyo.todolist.repository;

import com.team.heyyo.todolist.domain.TodoList;
import com.team.heyyo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
}
