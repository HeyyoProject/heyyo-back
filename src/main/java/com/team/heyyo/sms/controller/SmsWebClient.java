package com.team.heyyo.sms.controller;

import com.team.heyyo.sms.dto.SmsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class SmsWebClient {

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    WebClient webClient = WebClient.create();

    public Optional<SmsResponseDto> getSmsResponseDto(Long time, String payload) {
        Mono<SmsResponseDto> smsResponseDtoMono = webClient.post()
                .uri("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages")
                .contentType(APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(APPLICATION_JSON);
                    httpHeaders.set("x-ncp-apigw-timestamp", time.toString());
                    httpHeaders.set("x-ncp-iam-access-key", accessKey);
                    httpHeaders.set("x-ncp-apigw-signature-v2", makeSignature(time));
                })
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(SmsResponseDto.class);

        return Optional.ofNullable(smsResponseDtoMono.block());
    }

    private String makeSignature(Long time)  {
        try {
            String space = " ";
            String newLine = "\n";
            String method = "POST";
            String url = "/sms/v2/services/" + serviceId + "/messages";

            String timestamp = time.toString();

            String message = method +
                    space +
                    url +
                    newLine +
                    timestamp +
                    newLine +
                    accessKey;

            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes(UTF_8));

            return Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";

    }
}
