package com.team.heyyo.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.sms.dto.SmsMessageDto;
import com.team.heyyo.sms.dto.SmsResponseDto;
import com.team.heyyo.sms.dto.ToSmsServerRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmsWebClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SmsWebClient smsWebClient;

    @DisplayName("naver sms api 서버로 sms 요청을 보낸다.")
    @Test
    void sendRequestNaverSmsApi() throws JsonProcessingException {
        //given
        final String toPhoneNumber = "01073352306";
        final String content = "테스트 메시지 입니다.";
        final Long time = System.currentTimeMillis();

        SmsMessageDto smsMessageDto = SmsMessageDto.of(toPhoneNumber, content);
        List<SmsMessageDto> messages = setMessagesToList(smsMessageDto);

        ToSmsServerRequest smsPayload = setToSmsServerPayload(smsMessageDto, messages);
        String payload = objectMapper.writeValueAsString(smsPayload);

        //when
        Optional<SmsResponseDto> smsResponseDto = smsWebClient.getSmsResponseDto(time, payload);

        //then
        assertThat(smsResponseDto).isNotEmpty();
        assertThat(smsResponseDto.get().getStatusCode()).isEqualTo("202");
        assertThat(smsResponseDto.get().getStatusName()).isEqualTo("success");
    }

    private ToSmsServerRequest setToSmsServerPayload(SmsMessageDto smsMessageDto, List<SmsMessageDto> messages) {
        return ToSmsServerRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from("01073352306")
                .content(smsMessageDto.content())
                .messages(messages)
                .build();
    }

    private List<SmsMessageDto> setMessagesToList(SmsMessageDto smsMessageDto) {
        return new ArrayList<>(Collections.singleton(smsMessageDto));
    }


}