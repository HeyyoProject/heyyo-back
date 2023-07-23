package com.team.heyyo.auth.jwt.controller;

import com.team.heyyo.auth.jwt.dto.AccessTokenRequest;
import com.team.heyyo.auth.jwt.dto.AccessTokenResponse;
import com.team.heyyo.auth.jwt.service.TokenService;
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
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tokenService.createNewAccessToken(request));
    }

}
