package com.team.heyyo.community.post.support.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;
import com.team.heyyo.community.post.support.community.dto.SupportCommunityResponse;
import com.team.heyyo.community.post.support.tag.domain.QSupportCommunityTagData;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.team.heyyo.user.domain.QUser.user;
import static com.team.heyyo.community.post.support.community.domain.QSupportCommunity.supportCommunity;
import static com.team.heyyo.community.post.support.tag.domain.QSupportCommunityTagData.supportCommunityTagData;


@RequiredArgsConstructor
public class SupportCommunityRepositoryImpl implements CustomSupportCommunity {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SupportCommunity> findSupportCommunityResponseBySupportCommunityType(SupportCommunityType supportCommunityType) {
        return jpaQueryFactory.select(supportCommunity)
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityType.eq(supportCommunityType))
                .fetch();
    }

    @Override
    public long findSupportCommunityResponseCountBySupportCommunityType(SupportCommunityType supportCommunityType) {
        return jpaQueryFactory.select(supportCommunity.count())
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityType.eq(supportCommunityType))
                .fetchOne();
    }

    @Override
    public List<SupportCommunity> findSupportCommunityResponseById(long id) {
        return jpaQueryFactory.select(supportCommunity)
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityId.eq(id))
                .fetch();
    }

    @Override
    public List<SupportCommunity> findSupportCommunityResponseBySupportCommunityTypeAndSearch(SupportCommunityType supportCommunityType, String search) {
        return jpaQueryFactory.select(supportCommunity)
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityType.eq(supportCommunityType).and(supportCommunity.title.eq(search)).or(supportCommunity.content.eq(search)))
                .fetch();
    }

    @Override
    public long findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(SupportCommunityType supportCommunityType, String search) {
        return jpaQueryFactory.select(supportCommunity.count())
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityType.eq(supportCommunityType).and(supportCommunity.title.eq(search)).or(supportCommunity.content.eq(search)))
                .fetchOne();
    }

}
