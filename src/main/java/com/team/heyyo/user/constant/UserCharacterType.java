package com.team.heyyo.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserCharacterType {
  ALONE("고독"),
  CROWD("북적"),
  TIMID("소심"),
  COMMUNICATION("소통"),
  RESEARCH("영구"),
  BENEFICIAL("유익"),
  FOCUS("집중"),
  QUIET("한산");

  private final String koName;
}
