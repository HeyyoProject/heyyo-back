package com.team.heyyo.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "chatting_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chatting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingId;

    private Long toUserId;

    private Long fromUserId;

    private String session;

}
