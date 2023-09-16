package com.team.heyyo.group.study.repository.groupstudy;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.domain.QUser;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.team.heyyo.group.study.domain.QGroupStudy.groupStudy;
import static com.team.heyyo.group.study.domain.QGroupStudyLike.groupStudyLike;
import static com.team.heyyo.user.constant.Mbti.*;
import static com.team.heyyo.user.domain.QUser.user;

@RequiredArgsConstructor
public class GroupStudyRepositoryImpl implements GroupStudyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupStudy> selectRecentGroupStudies() {
        return queryFactory.selectFrom(groupStudy)
                .orderBy(groupStudy.createdAt.desc())
                .limit(12) // 최근 12개만 가져오기
                .fetch();
    }

    @Override
    public List<GroupStudy> findGroupStudiesOrderedByMostLikesFromToday() {
        LocalDateTime startOfToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        return queryFactory.select(groupStudy)
                .from(groupStudy)
                .leftJoin(groupStudyLike).on(groupStudyLike.groupStudyId.eq(groupStudy.groupStudyId))
                .where(groupStudyLike.createdAt.after(startOfToday)
                        .or(groupStudyLike.groupStudyId.isNull()))
                .groupBy(groupStudy.groupStudyId)
                .orderBy(groupStudyLike.groupStudyId.count().desc())
                .limit(12) // 최근 12개만 가져오기
                .fetch();
    }

    @Override
    public List<GroupStudy> selectRecentGroupStudyDetailListWithMbti(Mbti mbti, int limit) {
        return queryFactory.selectFrom(groupStudy)
                .where(groupStudy.mbti.eq(mbti))
                .orderBy(groupStudy.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<GroupStudy> selectMostLikeGroupStudyDetailListWithMbti(Long userId, Mbti mbti, int limit) {
        return queryFactory.selectFrom(groupStudy)
                .leftJoin(groupStudyLike).on(groupStudyLike.groupStudyId.eq(groupStudy.groupStudyId))
                .groupBy(groupStudy.groupStudyId)
                .orderBy(groupStudyLike.groupStudyId.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<GroupStudy> selectOppositeUserMbtiGroupStudyList(Long userId, int limit) {

        Mbti userMbti = getUserMbti(userId);

        BooleanExpression condition = null;

        switch (userMbti) {
            case Loneliness -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Focus))
            );
            case Communication -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Timid))
            );
            case Crowded -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Quiet))
            );
            case Quiet -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Crowded))
            );
            case Researching -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Useful))
            );
            case Useful -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Researching))
            );
            case Timid -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Communication))
            );
            case Focus -> condition = groupStudy.userId.in(
                    select(user.userId)
                            .from(user)
                            .where(user.mbtiType.eq(Loneliness))
            );
        }
        return queryFactory.selectFrom(groupStudy)
                .where(condition) // 생성한 조건을 적용합니다
                .limit(limit)
                .fetch();

    }

    @Override
    public Optional<GroupStudy> findGroupStudyAndChatById(long id) {
        GroupStudy result = queryFactory.select(groupStudy).from(groupStudy)
                .innerJoin(groupStudy.chat).fetchJoin()
                .where(groupStudy.groupStudyId.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private Mbti getUserMbti(Long userId) {
        return queryFactory.select(user.mbtiType)
                .where(user.userId.eq(userId))
                .fetchOne();
    }
}
