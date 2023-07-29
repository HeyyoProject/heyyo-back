package com.team.heyyo.todolist.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Table(name = "todo_list_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TodoList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long todoListId;

    private String data;

    private long userId;

    @UpdateTimestamp
    private Date completedDate;

    private boolean isComplete;
}
