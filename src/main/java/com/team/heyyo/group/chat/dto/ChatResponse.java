package com.team.heyyo.group.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatResponse {

    private long chatId;

    private String sender;

    private String message;

    private String sendTime;

    public static ChatResponse createChatResponse(ChatRequest chatMessage , String sender) {
        return ChatResponse.builder()
                .sender(sender)
                .sendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .message(chatMessage.getMessage())
                .build();
    }

}
