package com.team.heyyo.user.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserTypeRequest;
import com.team.heyyo.user.service.UserCharacterTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserCharacterTypeController {
  private final UserCharacterTypeService userCharacterTypeService;

  @PatchMapping("/character-types")
  public ResponseEntity<UserBaseResponse> patchCharacterType(
      @RequestBody @Valid  UserTypeRequest userTypeRequest,
      HttpServletRequest request
  ) {
    String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

    if (userCharacterTypeService.patchCharacterTypeWithAccessToken(
        userTypeRequest, accessToken)) {
      return ResponseEntity.ok().body(UserBaseResponse.of("캐릭터 타입이 저장 되었습니다."));
    }
      return ResponseEntity.badRequest().body(UserBaseResponse.of("캐릭터 타입 저장에 실패 하였습니다."));
  }

}
