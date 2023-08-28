package com.team.heyyo.group.study.repository.groupstudylike;

import com.team.heyyo.group.study.domain.GroupStudyLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupStudyLikeRepository extends JpaRepository<GroupStudyLike, Long>, GroupStudyLikeRepositoryCustom {
    boolean existsGroupStudyLikeByUserIdAndGroupStudyId(Long userId, Long groupStudyId);
}
