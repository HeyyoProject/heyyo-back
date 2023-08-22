package com.team.heyyo.user.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserBaseResponse {
  private String message;

  public static UserBaseResponse of(String message) {

    return new UserBaseResponse(message);

  }
}

