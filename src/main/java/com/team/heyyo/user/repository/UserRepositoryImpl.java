package com.team.heyyo.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.user.constant.UserCharacterType;
import com.team.heyyo.user.domain.QUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  @Override
  public Long updatePasswordWithEmail(String password, String email) {
    return queryFactory.update(QUser.user)
        .set(QUser.user.password, password)
        .where(QUser.user.email.eq(email))
        .execute();
  }

  @Override
  public Long updateCharacterTypeWithUserId(UserCharacterType userCharacterType, Long userId) {
    return queryFactory.update(QUser.user)
        .set(QUser.user.characterType, userCharacterType)
        .where(QUser.user.userId.eq(userId))
        .execute();
  }
}
