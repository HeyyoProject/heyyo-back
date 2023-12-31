package com.team.heyyo.todolist.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.todolist.domain.TodoList;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team.heyyo.todolist.domain.QTodoList.todoList;

@RequiredArgsConstructor
public class TodoListRepositoryImpl implements CustomTodoListRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<TodoList> findTodoListInProgressByUserId(long userId) {
        return jpaQueryFactory.select(todoList).from(todoList)
                .where(todoList.userId.eq(userId).and(todoList.isComplete.eq(false)))
                .fetch();
    }

    @Override
    public List<TodoList> findAchievedTodoListByUserId(long userId) {
        return jpaQueryFactory.select(todoList).from(todoList)
                .where(todoList.userId.eq(userId).and(todoList.isComplete.eq(true)))
                .fetch();
    }

    @Override
    public List<TodoList> getTodoListByDateAchieved(long userId , String date) {
        return jpaQueryFactory.select(todoList).from(todoList)
                .where(todoList.userId.eq(userId).and(todoList.isComplete.eq(true)).and(todoList.completedDate.eq(date)))
                .fetch();
    }

    @Override
    public List<TodoList> getTodoListForASpecificMonth(long userId , String month) {
        return jpaQueryFactory.select(todoList).from(todoList)
                .where(todoList.userId.eq(userId).and(todoList.isComplete.eq(true)).and(todoList.completedDate.startsWith(month)))
                .fetch();
    }
}
