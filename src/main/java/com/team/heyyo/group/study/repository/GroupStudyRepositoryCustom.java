package com.team.heyyo.group.study.repository;

import com.team.heyyo.group.study.domain.GroupStudy;

import java.util.List;

public interface GroupStudyRepositoryCustom {
    public List<GroupStudy> selectRecentGroupStudies();

    public List<GroupStudy> findGroupStudiesOrderedByMostLikesFromToday();
}

