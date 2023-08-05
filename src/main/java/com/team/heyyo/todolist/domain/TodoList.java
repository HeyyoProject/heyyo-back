package com.team.heyyo.todolist.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Table(name = "todo_list_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TodoList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoListId;

    private String data;

    private Long userId;

    private String completedDate;

    private Boolean isComplete;

    @PreUpdate
    public void preUpdate() {
        this.completedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void updateData(String data) {
        this.data = data;
    }

    public void updateIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public static TodoList of(String data , long userId) {
        return TodoList.builder()
                .data(data)
                .userId(userId)
                .isComplete(false)
                .build();
    }

}
