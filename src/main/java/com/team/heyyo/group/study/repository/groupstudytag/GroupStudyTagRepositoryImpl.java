package com.team.heyyo.group.study.repository.groupstudytag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.group.study.domain.QGroupStudyTag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team.heyyo.group.study.domain.QGroupStudyTag.groupStudyTag;

@RequiredArgsConstructor
public class GroupStudyTagRepositoryImpl implements GroupStudyTagRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<String> findByTagDataByGroupStudyId(Long groupStudyId) {
        return queryFactory.select(groupStudyTag.tagData)
                .from(groupStudyTag)
                .where(groupStudyTag.groupStudyId.eq(groupStudyId))
                .fetch();
    }
}
