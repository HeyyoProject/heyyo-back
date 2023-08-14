package com.team.heyyo.user.dto;

import com.team.heyyo.user.constant.UserCharacterType;
import jakarta.validation.constraints.NotNull;

public record UserTypeRequest(
    @NotNull UserCharacterType userCharacterType
) {
  public static UserTypeRequest of(UserCharacterType userCharacterType) {
    return new UserTypeRequest(userCharacterType);
  }
}
