package com.team.heyyo.group.reference.repository.reference;

import com.team.heyyo.group.reference.data.domain.Reference;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReferenceRepositoryCustom {
    List<Reference> findReferenceTodayMostLike();

    List<Reference> findAllOrderByViewsDesc(Pageable pageable);

    List<Reference> findAllOrderByScrapDesc(Pageable pageable);

    List<Reference> findAllOrderByCreatedAtDesc(Pageable pageable);
}
