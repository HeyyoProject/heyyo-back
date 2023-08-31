package com.team.heyyo.group.reference.repository.reference;

import com.querydsl.jpa.JPQLQuery;
import com.team.heyyo.group.reference.data.domain.Reference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.team.heyyo.group.reference.data.domain.QReference.reference;
import static com.team.heyyo.group.reference.data.domain.QReferenceScrap.referenceScrap;

public class ReferenceRepositoryImpl extends QuerydslRepositorySupport implements ReferenceRepositoryCustom {

    public ReferenceRepositoryImpl() {
        super(Reference.class);
    }


    @Override
    public List<Reference> findReferenceTodayMostLike() {
        LocalDateTime startOfToday = LocalDateTime.now();

        return from(reference)
                .select(reference)
                .leftJoin(referenceScrap).on(referenceScrap.referenceId.eq(reference.groupStudyReferenceId))
                .where(reference.createdAt.after(startOfToday)
                        .or(referenceScrap.referenceId.isNull()))
                .groupBy(reference.groupStudyReferenceId)
                .orderBy(referenceScrap.referenceId.count().desc())
                .limit(20) // 최근 20개만 가져오기
                .fetch();
    }

    @Override
    public List<Reference> findAllOrderByViewsDesc(Pageable pageable) {
        JPQLQuery<Reference> query = from(reference);

        return Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new IllegalArgumentException("Querydsl이 초기화되지 않았습니다."))
                .applyPagination(pageable, query)
                .orderBy(reference.views.desc())
                .fetch();
    }

    @Override
    public List<Reference> findAllOrderByScrapDesc(Pageable pageable) {
        JPQLQuery<Reference> query = from(reference)
                .select(reference)
                .leftJoin(referenceScrap).on(referenceScrap.referenceId.eq(reference.groupStudyReferenceId))  // "referenceScrap"가 "Reference" 엔티티 내부에 있는 속성이라고 가정합니다
                .groupBy(reference.groupStudyReferenceId);

        query = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new IllegalArgumentException("Querydsl이 초기화되지 않았습니다."))
                .applyPagination(pageable, query);

        return query
                .orderBy(referenceScrap.referenceId.count().desc())
                .fetch();
    }

    @Override
    public List<Reference> findAllOrderByCreatedAtDesc(Pageable pageable) {
        JPQLQuery<Reference> query = from(reference)
                .select(reference);

        return Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new IllegalArgumentException("Querydsl이 초기화되지 않았습니다."))
                .applyPagination(pageable, query)
                .orderBy(reference.createdAt.desc())
                .fetch();
    }


}
