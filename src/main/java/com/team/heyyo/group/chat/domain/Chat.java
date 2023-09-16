package com.team.heyyo.group.chat.domain;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.study.domain.GroupStudy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Chat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    private GroupStudy groupStudy;

    private String message;

    private String sendTime;

    private String sender;

    public static Chat createChat(ChatResponse chatResponse , GroupStudy groupStudy) {
        return Chat.builder()
                .message(chatResponse.getMessage())
                .sendTime(chatResponse.getSendTime())
                .groupStudy(groupStudy)
                .sender(chatResponse.getSender())
                .build();
    }
}
