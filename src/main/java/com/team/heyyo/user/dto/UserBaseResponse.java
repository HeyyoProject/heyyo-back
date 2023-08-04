package com.team.heyyo.user.dto;

public record UserBaseResponse(
    String message
) {

  public static UserBaseResponse of(String message) {
    return new UserBaseResponse(message);
  }
}

