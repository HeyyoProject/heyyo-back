package com.team.heyyo.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.sms.controller.SmsWebClient;
import com.team.heyyo.sms.dto.SmsResponseDto;
import com.team.heyyo.sms.dto.ToSmsServerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @InjectMocks
    SmsService smsService;

    @Spy
    ObjectMapper objectMapper;

    @Mock
    SmsWebClient smsWebClient;

    @DisplayName("sms전송에 성공하면, SmsResponseDto의 statusCode에 202을 담아 리턴한다")
    @Test
    void sendSmsAndReturnSmsResponseDto() throws JsonProcessingException {
        //given
        final String toPhoneNumber ="01012341234";

        doReturn("smsPayload")
                .when(objectMapper).writeValueAsString(any(ToSmsServerRequest.class));
        doReturn(Optional.of(SmsResponseDto.builder()
                        .statusName("success")
                        .statusCode("202")
                        .build()))
                .when(smsWebClient).getSmsResponseDto(anyLong(), anyString());

        //when
        SmsResponseDto smsResponseDto = smsService.sendSmsToUserWithCertificationNumber(toPhoneNumber);

        //then
        assertThat(smsResponseDto.getStatusCode()).isEqualTo("202");
        assertThat(smsResponseDto.getStatusName()).isEqualTo("success");
    }

    @DisplayName("sms전송에 실패하면, SmsResponseDto의 statusCode에 404을 담아 리턴한다")
    @Test
    void sendSmsFail() throws JsonProcessingException {
        //given
        final String toPhoneNumber ="01012341234";

        doReturn("smsPayload")
                .when(objectMapper).writeValueAsString(any(ToSmsServerRequest.class));
        doReturn(Optional.empty())
                .when(smsWebClient).getSmsResponseDto(anyLong(), anyString());

        //when
        SmsResponseDto smsResponseDto = smsService.sendSmsToUserWithCertificationNumber(toPhoneNumber);

        //then
        assertThat(smsResponseDto.getStatusCode()).isEqualTo("404");
    }

}