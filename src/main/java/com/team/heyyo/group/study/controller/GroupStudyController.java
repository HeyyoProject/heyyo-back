package com.team.heyyo.group.study.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.group.study.dto.GroupStudyListResponse;
import com.team.heyyo.group.study.service.GroupStudyDetailPageListService;
import com.team.heyyo.group.study.service.GroupStudyMainPageListService;
import com.team.heyyo.user.constant.Mbti;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/group-studies")
@RequiredArgsConstructor
@RestController
public class GroupStudyController {

    private final GroupStudyMainPageListService groupStudyMainPageListService;
    private final GroupStudyDetailPageListService groupStudyDetailPageListService;

    @GetMapping("/recent")
    public ResponseEntity<List<GroupStudyListResponse>> getRecentGroupStudyList(@AccessToken String accessToken) {

        List<GroupStudyListResponse> recentGroupStudyList = groupStudyMainPageListService.getRecentGroupStudyList(accessToken);
        return ResponseEntity.ok(recentGroupStudyList);
    }

    @GetMapping("/best")
    public ResponseEntity<List<GroupStudyListResponse>> getBestGroupStudyList(@AccessToken String accessToken) {
        List<GroupStudyListResponse> bestGroupListFromToday = groupStudyMainPageListService.getBestGroupStudyListFromToday(accessToken);
        return ResponseEntity.ok(bestGroupListFromToday);
    }

    @GetMapping("/detail/recent/{mbti}")
    public ResponseEntity<List<GroupStudyListResponse>> getRecentMbtiGroupStudyList(
            @AccessToken String accessToken,
            @PathVariable Mbti mbti
    ) {
        List<GroupStudyListResponse> groupStudyDetailList = groupStudyDetailPageListService.getRecentGroupStudyDetailList(accessToken, mbti);
        return ResponseEntity.ok(groupStudyDetailList);
    }

    @GetMapping("/detail/best/{mbti}")
    public ResponseEntity<List<GroupStudyListResponse>> getBestMbtiGroupStudyList(
            @AccessToken String accessToken,
            @PathVariable Mbti mbti
    ) {
        List<GroupStudyListResponse> groupStudyDetailList = groupStudyDetailPageListService.getMostLikeGroupStudyDetailList(accessToken, mbti);
        return ResponseEntity.ok(groupStudyDetailList);
    }

    @GetMapping("/detail/opposite")
    public ResponseEntity<List<GroupStudyListResponse>> getBestMbtiGroupStudyList(
            @AccessToken String accessToken
    ) {
        List<GroupStudyListResponse> groupStudyDetailList = groupStudyDetailPageListService.getOppositeUserMbtiGroupStudyList(accessToken);
        return ResponseEntity.ok(groupStudyDetailList);
    }
}
