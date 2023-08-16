package com.team.heyyo.todolist.repository;

import com.team.heyyo.todolist.domain.TodoList;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CustomTodoListRepository {

    List<TodoList> findTodoListInProgressByUserId(long userId);

    List<TodoList> findAchievedTodoListByUserId(long userId);

    List<TodoList> getTodoListByDateAchieved(long userId , String date);

    List<TodoList> getTodoListForASpecificMonth(long userId , String month);

}
