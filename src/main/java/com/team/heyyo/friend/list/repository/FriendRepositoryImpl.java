package com.team.heyyo.friend.list.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.user.domain.QUser;
import lombok.RequiredArgsConstructor;

import static com.team.heyyo.user.domain.QUser.user;
import static com.team.heyyo.friend.list.domain.QFriend.friend;
import java.util.List;

@RequiredArgsConstructor
public class FriendRepositoryImpl implements CustomFriendRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<UserResponse> findFriendByUserId(long userId) {
        return jpaQueryFactory.select(Projections.constructor(UserResponse.class,
                        user.userId, user.email , user.name , user.password , user.phone ,
                        user.mbtiType , user.birth , user.nickname))
                .from(user)
                .where(user.userId.eqAny(
                        JPAExpressions.select(friend.toUserId)
                                .from(friend)
                                .where(friend.fromUserId.eq(userId))
                ))
                .fetch();

    }
}
