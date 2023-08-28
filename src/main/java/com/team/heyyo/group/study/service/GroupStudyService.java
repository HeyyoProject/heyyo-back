package com.team.heyyo.group.study.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.domain.GroupStudyTag;
import com.team.heyyo.group.study.dto.GroupStudyResponse;
import com.team.heyyo.group.study.repository.groupstudylike.GroupStudyLikeRepository;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import com.team.heyyo.group.study.repository.groupstudytag.GroupStudyTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class GroupStudyService {

    private final GroupStudyRepository groupStudyRepository;
    private final GroupStudyTagRepository groupStudyTagRepository;
    private final GroupStudyLikeRepository groupStudyLikeRepository;
    private final TokenProvider tokenProvider;

    public List<GroupStudyResponse> getRecentGroupStudyList(String accessToken) {

//        FIXME : 회원이 세션에 몇명 로그인 됐는지 바뀔수 있기 때문에 일단 임시로 랜덤값 삽입
        int randomNumber = ThreadLocalRandom.current().nextInt(100, 1000);
        Long userId = tokenProvider.getUserId(accessToken);

        List<GroupStudyResponse> groupStudyResponses = new ArrayList<>();

        List<GroupStudy> groupStudies = groupStudyRepository.selectRecentGroupStudies();
        for (GroupStudy groupStudy : groupStudies) {
            boolean isUserLikedThisGroupStudy = isUserLikedThisGroupStudy(userId, groupStudy);

            List<String> groupStudyTagList = getGroupStudyTagList(groupStudy);
            groupStudyResponses
                    .add(GroupStudyResponse.of(groupStudy.getTitle(), groupStudyTagList, randomNumber, isUserLikedThisGroupStudy));
        }
        return groupStudyResponses;
    }

    public List<GroupStudyResponse> getBestGroupStudyListFromToday(String accessToken) {

        //        FIXME : 회원이 세션에 몇명 로그인 됐는지 바뀔수 있기 때문에 일단 임시로 랜덤값 삽입
        int randomNumber = ThreadLocalRandom.current().nextInt(100, 1000);
        Long userId = tokenProvider.getUserId(accessToken);
        List<GroupStudyResponse> groupStudyResponses = new ArrayList<>();

        List<GroupStudy> groupStudiesOrderedByMostLikesFromToday = groupStudyRepository.findGroupStudiesOrderedByMostLikesFromToday();
        for (GroupStudy groupStudy : groupStudiesOrderedByMostLikesFromToday) {
            boolean isUserLikedThisGroupStudy = isUserLikedThisGroupStudy(userId, groupStudy);

            List<String> groupStudyTagList = getGroupStudyTagList(groupStudy);
            groupStudyResponses
                    .add(GroupStudyResponse.of(groupStudy.getTitle(), groupStudyTagList, randomNumber, isUserLikedThisGroupStudy));
        }
        return groupStudyResponses;

    }


    private boolean isUserLikedThisGroupStudy(Long userId, GroupStudy groupStudy) {
        return groupStudyLikeRepository.existsGroupStudyLikeByUserIdAndGroupStudyId(userId, groupStudy.getGroupStudyId());
    }

    private List<String> getGroupStudyTagList(GroupStudy groupStudy) {
        return groupStudyTagRepository.findByTagDataByGroupStudyId(groupStudy.getGroupStudyId());
    }
}
