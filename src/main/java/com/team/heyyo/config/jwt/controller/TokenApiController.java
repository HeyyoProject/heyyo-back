package com.team.heyyo.config.jwt.controller;

import com.team.heyyo.config.jwt.dto.AccessTokenRequest;
import com.team.heyyo.config.jwt.dto.AccessTokenResponse;
import com.team.heyyo.config.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/tokens")
    public ResponseEntity<AccessTokenResponse> createNewAccessToken(
            @RequestBody AccessTokenRequest request
    ) {
        AccessTokenResponse newAccessToken = tokenService.createNewAccessToken(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newAccessToken);
    }

}
