package com.team.heyyo.group.study.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.dto.GroupStudyListResponse;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import com.team.heyyo.group.study.repository.groupstudylike.GroupStudyLikeRepository;
import com.team.heyyo.group.study.repository.groupstudytag.GroupStudyTagRepository;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class GroupStudyMainPageListService {

    private final GroupStudyRepository groupStudyRepository;
    private final GroupStudyTagRepository groupStudyTagRepository;
    private final GroupStudyLikeRepository groupStudyLikeRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public List<GroupStudyListResponse> getRecentGroupStudyList(String accessToken) {
        int randomNumber = getRandomNumber();
        Long userId = tokenProvider.getUserId(accessToken);

        List<GroupStudyListResponse> groupStudyListResponseList = new ArrayList<>();

        List<GroupStudy> groupStudies = groupStudyRepository.selectRecentGroupStudies();
        for (GroupStudy groupStudy : groupStudies) {
            boolean isUserLikedThisGroupStudy = isUserLikedThisGroupStudy(userId, groupStudy);

            List<String> groupStudyTagList = getGroupStudyTagList(groupStudy);
            groupStudyListResponseList
                    .add(GroupStudyListResponse.of(groupStudy.getTitle(), groupStudyTagList, randomNumber, isUserLikedThisGroupStudy));
        }
        return groupStudyListResponseList;
    }

    public List<GroupStudyListResponse> getBestGroupStudyListFromToday(String accessToken) {
        int randomNumber = getRandomNumber();
        Long userId = tokenProvider.getUserId(accessToken);
        List<GroupStudyListResponse> groupStudyListResponseList = new ArrayList<>();

        List<GroupStudy> groupStudiesOrderedByMostLikesFromToday = groupStudyRepository.findGroupStudiesOrderedByMostLikesFromToday();
        for (GroupStudy groupStudy : groupStudiesOrderedByMostLikesFromToday) {
            boolean isUserLikedThisGroupStudy = isUserLikedThisGroupStudy(userId, groupStudy);

            List<String> groupStudyTagList = getGroupStudyTagList(groupStudy);
            groupStudyListResponseList
                    .add(GroupStudyListResponse.of(groupStudy.getTitle(), groupStudyTagList, randomNumber, isUserLikedThisGroupStudy));
        }
        return groupStudyListResponseList;

    }

    public List<GroupStudyListResponse> getRecommendGroupStudyList(String accessToken) {
        int randomNumber = getRandomNumber();
        Long userId = tokenProvider.getUserId(accessToken);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));

        List<GroupStudyListResponse> groupStudyListResponseList = new ArrayList<>();
        List<GroupStudy> groupStudies = groupStudyRepository.selectRecentGroupStudyDetailListWithMbti(userId, user.getMbtiType(), 20);
        groupStudies = getRandomGroupStudies(groupStudies, 8);

        for (GroupStudy groupStudy : groupStudies) {
            boolean isUserLikedThisGroupStudy = isUserLikedThisGroupStudy(userId, groupStudy);

            List<String> groupStudyTagList = getGroupStudyTagList(groupStudy);
            groupStudyListResponseList
                    .add(GroupStudyListResponse.of(groupStudy.getTitle(), groupStudyTagList, randomNumber, isUserLikedThisGroupStudy));
        }
        return groupStudyListResponseList;
    }

    //        FIXME : 회원이 세션에 몇명 로그인 됐는지 바뀔수 있기 때문에 일단 임시로 랜덤값 삽입
    private int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(100, 1000);
    }

    private List<GroupStudy> getRandomGroupStudies(List<GroupStudy> groupStudies, int numberOfElementsToSelect) {
        List<GroupStudy> randomGroupStudies = new ArrayList<>(groupStudies);
        Random random = new Random();
        int remaining = randomGroupStudies.size();

        while (remaining > numberOfElementsToSelect) {
            int randomIndex = random.nextInt(remaining);
            randomGroupStudies.remove(randomIndex);
            remaining--;
        }

        return randomGroupStudies;
    }


    private boolean isUserLikedThisGroupStudy(Long userId, GroupStudy groupStudy) {
        return groupStudyLikeRepository.existsGroupStudyLikeByUserIdAndGroupStudyId(userId, groupStudy.getGroupStudyId());
    }

    private List<String> getGroupStudyTagList(GroupStudy groupStudy) {
        return groupStudyTagRepository.findByTagDataByGroupStudyId(groupStudy.getGroupStudyId());
    }

}
