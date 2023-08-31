package com.team.heyyo.group.study.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.group.study.dto.GroupStudyListResponse;
import com.team.heyyo.group.study.service.GroupStudyDetailPageListService;
import com.team.heyyo.group.study.service.GroupStudyMainPageListService;
import com.team.heyyo.user.constant.Mbti;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<List<GroupStudyListResponse>> getRecentGroupStudyList(HttpServletRequest request) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        List<GroupStudyListResponse> recentGroupStudyList = groupStudyMainPageListService.getRecentGroupStudyList(accessToken);
        return ResponseEntity.ok(recentGroupStudyList);
    }

    @GetMapping("/best")
    public ResponseEntity<List<GroupStudyListResponse>> getBestGroupStudyList(HttpServletRequest request) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        List<GroupStudyListResponse> bestGroupListFromToday = groupStudyMainPageListService.getBestGroupStudyListFromToday(accessToken);
        return ResponseEntity.ok(bestGroupListFromToday);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<GroupStudyListResponse>> getRecommendStudyList(HttpServletRequest request) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        List<GroupStudyListResponse> recommendGroupStudyList = groupStudyMainPageListService.getRecommendGroupStudyList(accessToken);

        return ResponseEntity.ok(recommendGroupStudyList);
    }

    @GetMapping("/detail/recent/{mbti}")
    public ResponseEntity<List<GroupStudyListResponse>> getRecentMbtiGroupStudyList(
            @PathVariable Mbti mbti,
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        List<GroupStudyListResponse> groupStudyDetailList = groupStudyDetailPageListService.getRecentGroupStudyDetailList(accessToken, mbti);
        return ResponseEntity.ok(groupStudyDetailList);
    }

    @GetMapping("/detail/best/{mbti}")
    public ResponseEntity<List<GroupStudyListResponse>> getBestMbtiGroupStudyList(
            @PathVariable Mbti mbti,
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        List<GroupStudyListResponse> groupStudyDetailList = groupStudyDetailPageListService.getMostLikeGroupStudyDetailList(accessToken, mbti);
        return ResponseEntity.ok(groupStudyDetailList);
    }

    @GetMapping("/detail/opposite")
    public ResponseEntity<List<GroupStudyListResponse>> getBestMbtiGroupStudyList(
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        List<GroupStudyListResponse> groupStudyDetailList = groupStudyDetailPageListService.getOppositeUserMbtiGroupStudyList(accessToken);
        return ResponseEntity.ok(groupStudyDetailList);
    }
}
