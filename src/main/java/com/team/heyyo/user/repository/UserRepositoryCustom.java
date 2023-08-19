package com.team.heyyo.user.repository;

import com.team.heyyo.user.constant.UserCharacterType;

public interface UserRepositoryCustom {

  Long updatePasswordWithEmail(String password, String email);

  Long updatePasswordWithId(String password, Long userId);

  Long updateNickNameWithId(String nickname, Long userId);

  Long updateCharacterTypeWithUserId(UserCharacterType userCharacterType,Long userId);
}
