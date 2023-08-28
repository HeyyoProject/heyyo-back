package com.team.heyyo.group.study.repository.groupstudytag;

import com.team.heyyo.group.study.domain.GroupStudyTag;

import java.util.List;

public interface GroupStudyTagRepositoryCustom {
    List<String> findByTagDataByGroupStudyId(Long groupStudyId);
}
