package com.team.heyyo.group.study.repository.groupstudytag;

import com.team.heyyo.group.study.domain.GroupStudyTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupStudyTagRepository extends JpaRepository<GroupStudyTag, Long>, GroupStudyTagRepositoryCustom {

    List<GroupStudyTag> findByGroupStudyId(Long groupStudyId);

}

