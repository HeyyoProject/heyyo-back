package com.team.heyyo.friend.request.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.friend.dto.UserResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team.heyyo.friend.request.entity.QFriendRequest.friendRequest;
import static com.team.heyyo.user.domain.QUser.user;

@RequiredArgsConstructor
public class FriendRequestRepositoryImpl implements CustomFriendRequestRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserResponse> findFriendRequestByUserId(long userId) {
        return jpaQueryFactory.select(Projections.constructor(UserResponse.class,
                        user.userId, user.email, user.name, user.password, user.phone,
                        user.mbtiType, user.birth, user.nickname))
                .from(user)
                .where(user.userId.eqAny(
                        JPAExpressions.select(friendRequest.toUserId)
                                .from(friendRequest)
                                .where(friendRequest.fromUserId.eq(userId))
                ))
                .fetch();
    }
}
