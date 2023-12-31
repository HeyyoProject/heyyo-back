package com.team.heyyo.sms.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ToSmsServerRequest {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<SmsMessageDto> messages;

}
