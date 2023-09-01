package com.team.heyyo.user.dto;

import com.team.heyyo.user.constant.Mbti;

public record UserTypeRequest(
        Mbti mbti
) {
  public static UserTypeRequest of(Mbti mbti) {
    return new UserTypeRequest(mbti);
  }
}
