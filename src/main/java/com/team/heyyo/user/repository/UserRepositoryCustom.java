package com.team.heyyo.user.repository;

import com.team.heyyo.user.constant.UserCharacterType;

public interface UserRepositoryCustom {

  Long updatePasswordWithEmail(String password, String email);

  Long updateCharacterTypeWithUserId(UserCharacterType userCharacterType,Long userId);
}
