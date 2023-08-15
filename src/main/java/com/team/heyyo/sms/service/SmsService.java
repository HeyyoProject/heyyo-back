package com.team.heyyo.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.sms.controller.SmsWebClient;
import com.team.heyyo.sms.dto.SmsMessageDto;
import com.team.heyyo.sms.dto.SmsResponseDto;
import com.team.heyyo.sms.dto.ToSmsServerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {

    private final ObjectMapper objectMapper;
    private final SmsWebClient smsWebClient;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    WebClient webClient = WebClient.create();

    public SmsResponseDto sendSmsToUserWithCertificationNumber(String to) {

        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        String content = "HeyYo 인증번호 입니다. \n인증번호는 " + randomNumber + " 입니다.";

        return sendSms(SmsMessageDto.of(to, content))
                .map(responseDto -> {
                    responseDto.setCertificationNumber(randomNumber);
                    return responseDto;
                })
                .orElse(SmsResponseDto.builder()
                        .statusCode("404")
                        .build());
    }

    public Optional<SmsResponseDto> sendSms(SmsMessageDto smsMessageDto) {
        try {
            Long time = System.currentTimeMillis();

            List<SmsMessageDto> messages = setMessagesToList(smsMessageDto);

            ToSmsServerRequest smsPayload = setToSmsServerPayload(smsMessageDto, messages);
            String payload = objectMapper.writeValueAsString(smsPayload);

            return smsWebClient.getSmsResponseDto(time, payload);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    private ToSmsServerRequest setToSmsServerPayload(SmsMessageDto smsMessageDto, List<SmsMessageDto> messages) {
        return ToSmsServerRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content(smsMessageDto.content())
                .messages(messages)
                .build();
    }

    private List<SmsMessageDto> setMessagesToList(SmsMessageDto smsMessageDto) {
        return new ArrayList<>(Collections.singleton(smsMessageDto));
    }


}
