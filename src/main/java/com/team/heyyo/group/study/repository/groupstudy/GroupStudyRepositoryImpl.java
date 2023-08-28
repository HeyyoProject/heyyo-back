package com.team.heyyo.group.study.repository.groupstudy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.group.study.domain.GroupStudy;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.team.heyyo.group.study.domain.QGroupStudy.groupStudy;
import static com.team.heyyo.group.study.domain.QGroupStudyLike.groupStudyLike;

@RequiredArgsConstructor
public class GroupStudyRepositoryImpl implements GroupStudyRepositoryCustom{

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

}
