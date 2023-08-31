package com.team.heyyo.group.reference.repository.referenceTag;

import com.team.heyyo.group.reference.data.domain.ReferenceTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferenceTagRepository extends JpaRepository<ReferenceTag, Long> {

    List<ReferenceTag> findByReferenceId(Long referenceId);
}
