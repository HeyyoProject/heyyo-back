package com.team.heyyo.group.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {

    private String sender;

    private String senderImage;

    private String message;

    private long meetingId;

    private MessageType messageType;

}