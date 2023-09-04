package com.team.heyyo.community.post.support.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.team.heyyo.community.post.support.community.domain.QSupportCommunity.supportCommunity;
import static com.team.heyyo.user.domain.QUser.user;


@RequiredArgsConstructor
public class SupportCommunityRepositoryImpl implements CustomSupportCommunity {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SupportCommunity> findSupportCommunityResponseBySupportCommunityType(Pageable pageable , SupportCommunityType supportCommunityType) {
        return jpaQueryFactory.select(supportCommunity)
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityType.eq(supportCommunityType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
    public List<SupportCommunity> findSupportCommunityResponseBySupportCommunityTypeAndSearch(Pageable pageable , SupportCommunityType supportCommunityType, String search) {
        return jpaQueryFactory.select(supportCommunity)
                .from(supportCommunity)
                .innerJoin(user).on(user.userId.eq(supportCommunity.userId))
                .where(supportCommunity.supportCommunityType.eq(supportCommunityType).and(supportCommunity.title.eq(search)).or(supportCommunity.content.eq(search)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
