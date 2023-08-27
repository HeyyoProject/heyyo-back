package com.team.heyyo.group.study.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.group.study.dto.GroupStudyResponse;
import com.team.heyyo.group.study.service.GroupStudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/group-studies")
@RequiredArgsConstructor
@RestController
public class GroupStudyController {

    private final GroupStudyService groupStudyService;

    @GetMapping("/recent")
    public ResponseEntity<List<GroupStudyResponse>> getRecentGroupStudyList(@AccessToken String accessToken) {

        List<GroupStudyResponse> recentGroupStudyList = groupStudyService.getRecentGroupStudyList(accessToken);
        return ResponseEntity.ok(recentGroupStudyList);
    }

    @GetMapping("/best")
    public ResponseEntity<List<GroupStudyResponse>> getBestGroupStudyList(@AccessToken String accessToken) {
        List<GroupStudyResponse> bestGroupListFromToday = groupStudyService.getBestGroupStudyListFromToday(accessToken);
        return ResponseEntity.ok(bestGroupListFromToday);
    }
}
