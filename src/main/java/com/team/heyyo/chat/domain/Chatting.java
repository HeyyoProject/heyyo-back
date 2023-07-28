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
    private long chattingKey;

    private long toUserKey;

    private long fromUserKey;

    private String session;

}
