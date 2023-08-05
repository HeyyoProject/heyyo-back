package com.team.heyyo.todolist.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoListMessageResponse {
    private String message;

    public static TodoListMessageResponse of (String message) {
        return TodoListMessageResponse.builder()
                .message(message)
                .build();
    }
}
