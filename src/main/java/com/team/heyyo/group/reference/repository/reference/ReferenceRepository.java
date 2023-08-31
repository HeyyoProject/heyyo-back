package com.team.heyyo.group.reference.repository.reference;

import com.team.heyyo.group.reference.data.domain.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long>, ReferenceRepositoryCustom {
}
