package com.team.heyyo.community.post.support.community.controller;

import com.team.heyyo.common.AccessToken;
import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.dto.CommentRequest;
import com.team.heyyo.community.post.support.community.dto.NewSupportCommunityRequest;
import com.team.heyyo.community.post.support.community.dto.SupportCommunityRequest;
import com.team.heyyo.community.post.support.community.service.SupportCommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportCommunityController {

    private final SupportCommunityService supportCommunityService;

    @PostMapping
    public ResponseEntity saveSupportCommunity(@AccessToken String accessToken , @RequestBody NewSupportCommunityRequest request) {
        supportCommunityService.saveSupportCommunity(accessToken , request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity updateCommentAndIsSolved(@PathVariable long postId , @RequestBody CommentRequest commentRequest) {
        supportCommunityService.updateCommentAndIsSolved(postId, commentRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity findSupportCommunityResponseBySupportCommunityType(Pageable pageable, @RequestBody SupportCommunityRequest supportCommunityRequest) {
        List<SupportCommunity> result = supportCommunityService.findSupportCommunityResponseBySupportCommunityType(pageable , supportCommunityRequest);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/count")
    public ResponseEntity findSupportCommunityResponseCountBySupportCommunityType(@RequestBody SupportCommunityRequest supportCommunityRequest) {
        long result = supportCommunityService.findSupportCommunityResponseCountBySupportCommunityType(supportCommunityRequest);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/search")
    public ResponseEntity findSupportCommunityResponseBySupportCommunityTypeAndSearch(
            Pageable pageable, @RequestParam String search , @RequestBody SupportCommunityRequest request) {
        List<SupportCommunity> result = supportCommunityService.findSupportCommunityResponseBySupportCommunityTypeAndSearch(pageable , request , search);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/search/count")
    public ResponseEntity findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(
            @RequestParam String search , @RequestBody SupportCommunityRequest request) {
        long result = supportCommunityService.findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(request , search);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{postId}")
    public ResponseEntity findSupportCommunityResponseById(@PathVariable long postId) {
        SupportCommunity result = supportCommunityService.findSupportCommunityResponseById(postId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
