package com.team.heyyo.group.study.repository.groupstudy;

import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.user.constant.Mbti;

import java.util.List;

public interface GroupStudyRepositoryCustom {
    public List<GroupStudy> selectRecentGroupStudies();

    public List<GroupStudy> findGroupStudiesOrderedByMostLikesFromToday();
    public List<GroupStudy> selectRecentGroupStudyDetailListWithMbti(Mbti mbti, int limit);

    List<GroupStudy> selectMostLikeGroupStudyDetailListWithMbti(Long userId, Mbti mbti, int limit);

    List<GroupStudy> selectOppositeUserMbtiGroupStudyList(Long userId, int limit);
}

