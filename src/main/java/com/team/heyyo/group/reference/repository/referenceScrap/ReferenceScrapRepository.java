package com.team.heyyo.group.reference.repository.referenceScrap;

import com.team.heyyo.group.reference.data.domain.ReferenceScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReferenceScrapRepository extends JpaRepository<ReferenceScrap, Long>  {

    Optional<ReferenceScrap> findByReferenceIdAndUserId(Long referenceId, Long userId);

    boolean existsByReferenceIdAndUserId(Long referenceId, Long userId);
}
