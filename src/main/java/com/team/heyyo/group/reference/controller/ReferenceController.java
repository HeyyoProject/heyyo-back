package com.team.heyyo.group.reference.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.group.reference.constant.ReferenceOrderBy;
import com.team.heyyo.group.reference.dto.ReferenceListResponse;
import com.team.heyyo.group.reference.dto.ReferenceSwiperListResponse;
import com.team.heyyo.group.reference.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group-study/reference")
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/best")
    public ResponseEntity<List<ReferenceSwiperListResponse>> todayMostLikeReference(@AccessToken String accessToken) {

        List<ReferenceSwiperListResponse> referenceList = referenceService.findTodayMostLikeReference(accessToken);

        return ResponseEntity.ok().body(referenceList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReferenceListResponse>> getReferenceList(
            @AccessToken String accessToken,
            @RequestParam(defaultValue = "POPULAR") ReferenceOrderBy orderBy,
            @PageableDefault(size = 12, direction = DESC) Pageable pageable
    ) {
        List<ReferenceListResponse> referenceList = referenceService.findReferenceList(orderBy, pageable, accessToken);

        return ResponseEntity.ok().body(referenceList);
    }

}
