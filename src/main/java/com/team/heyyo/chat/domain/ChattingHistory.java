package com.team.heyyo.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "chatting_history_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChattingHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chattingHistoryId;

    private long toUserId;

    private long fromUserId;

    private String history;

}
