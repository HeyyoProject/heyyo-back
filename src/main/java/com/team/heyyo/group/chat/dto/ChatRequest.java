package com.team.heyyo.group.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {

    private String accessToken;

    private String message;

    private long groupStudyId;

    private MessageType messageType;

}