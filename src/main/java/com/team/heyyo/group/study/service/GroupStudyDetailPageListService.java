package com.team.heyyo.group.study.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.dto.GroupStudyListResponse;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import com.team.heyyo.group.study.repository.groupstudylike.GroupStudyLikeRepository;
import com.team.heyyo.group.study.repository.groupstudytag.GroupStudyTagRepository;
import com.team.heyyo.user.constant.Mbti;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


// FIXME 그룹공부방 당 좋아요 수 로직 추가 핋요
@RequiredArgsConstructor
@Service
public class GroupStudyDetailPageListService {

    private final GroupStudyRepository groupStudyRepository;
    private final GroupStudyTagRepository groupStudyTagRepository;
    private final GroupStudyLikeRepository groupStudyLikeRepository;
    private final TokenProvider tokenProvider;

    public List<GroupStudyListResponse> getRecentGroupStudyDetailList(String accessToken, Mbti mbti) {
        int randomNumber = getRandomNumber();
        Long userId = tokenProvider.getUserId(accessToken);

        List<GroupStudyListResponse> groupStudyListResponseList = new ArrayList<>();
        List<GroupStudy> groupStudies = groupStudyRepository.selectRecentGroupStudyDetailListWithMbti(mbti, 6);

        return getGroupStudyListResponses(randomNumber, userId, groupStudyListResponseList, groupStudies);
    }

    public List<GroupStudyListResponse> getMostLikeGroupStudyDetailList(String accessToken, Mbti mbti) {
        int randomNumber = getRandomNumber();
        Long userId = tokenProvider.getUserId(accessToken);

        List<GroupStudyListResponse> groupStudyListResponseList = new ArrayList<>();
        List<GroupStudy> groupStudies = groupStudyRepository.selectMostLikeGroupStudyDetailListWithMbti(userId, mbti, 6);

        return getGroupStudyListResponses(randomNumber, userId, groupStudyListResponseList, groupStudies);
    }

    public List<GroupStudyListResponse> getOppositeUserMbtiGroupStudyList(String accessToken) {
        int randomNumber = getRandomNumber();
        Long userId = tokenProvider.getUserId(accessToken);

        List<GroupStudyListResponse> groupStudyListResponseList = new ArrayList<>();
        List<GroupStudy> groupStudies = groupStudyRepository.selectOppositeUserMbtiGroupStudyList(userId, 8);

        return getGroupStudyListResponses(randomNumber, userId, groupStudyListResponseList, groupStudies);
    }


    private List<GroupStudyListResponse> getGroupStudyListResponses(int randomNumber, Long userId, List<GroupStudyListResponse> groupStudyListResponseList, List<GroupStudy> groupStudies) {
        for (GroupStudy groupStudy : groupStudies) {
            boolean isUserLikedThisGroupStudy = isUserLikedThisGroupStudy(userId, groupStudy);

            List<String> groupStudyTagList = getGroupStudyTagList(groupStudy);
            addGroupStudyResponseToGroupStudyResponseList(randomNumber, groupStudyListResponseList, groupStudy, isUserLikedThisGroupStudy, groupStudyTagList);
        }
        return groupStudyListResponseList;
    }

    private static void addGroupStudyResponseToGroupStudyResponseList(int randomNumber, List<GroupStudyListResponse> groupStudyListResponseList, GroupStudy groupStudy, boolean isUserLikedThisGroupStudy, List<String> groupStudyTagList) {
        groupStudyListResponseList
            .add(GroupStudyListResponse.of(groupStudy.getTitle(), groupStudyTagList, randomNumber, isUserLikedThisGroupStudy));
    }


    //        FIXME : 회원이 세션에 몇명 로그인 됐는지 바뀔수 있기 때문에 일단 임시로 랜덤값 삽입
    private int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(100, 1000);
    }

    private boolean isUserLikedThisGroupStudy(Long userId, GroupStudy groupStudy) {
        return groupStudyLikeRepository.existsGroupStudyLikeByUserIdAndGroupStudyId(userId, groupStudy.getGroupStudyId());
    }

    private List<String> getGroupStudyTagList(GroupStudy groupStudy) {
        return groupStudyTagRepository.findByTagDataByGroupStudyId(groupStudy.getGroupStudyId());
    }


}
