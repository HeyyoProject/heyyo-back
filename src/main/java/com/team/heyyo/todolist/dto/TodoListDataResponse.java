package com.team.heyyo.todolist.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoListDataResponse<T> {
    private T data;

    public static <T>TodoListDataResponse of (T data) {
        return TodoListDataResponse.builder()
                .data(data)
                .build();
    }
}
