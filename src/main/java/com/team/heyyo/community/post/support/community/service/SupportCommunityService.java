package com.team.heyyo.community.post.support.community.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.dto.CommentRequest;
import com.team.heyyo.community.post.support.community.dto.NewSupportCommunityRequest;
import com.team.heyyo.community.post.support.community.dto.SupportCommunityRequest;
import com.team.heyyo.community.post.support.community.exception.SupportCommunityException;
import com.team.heyyo.community.post.support.community.repository.SupportCommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team.heyyo.community.post.support.community.domain.SupportCommunity.createSupportCommunity;
import static com.team.heyyo.community.post.support.tag.domain.SupportCommunityTagData.buildTagData;

@Service
@RequiredArgsConstructor
public class SupportCommunityService {

    private final SupportCommunityRepository supportCommunityRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public void saveSupportCommunity(String accessToken , NewSupportCommunityRequest request) {
        long userId = tokenProvider.getUserId(accessToken);

        SupportCommunity supportCommunity = createSupportCommunity(userId , request);
        for(String tag : request.getTag()) {
            supportCommunity.addTagData(buildTagData(tag));
        }

        supportCommunityRepository.save(supportCommunity);
    }

    @Transactional
    public void updateCommentAndIsSolved(long id , CommentRequest commentRequest) {
        SupportCommunity supportCommunity = findSupportCommunityResponseById(id);

        supportCommunity.updateComment(commentRequest.getMessage());
    }

    public List<SupportCommunity> findSupportCommunityResponseBySupportCommunityType(SupportCommunityRequest supportCommunityRequest) {
        return supportCommunityRepository.findSupportCommunityResponseBySupportCommunityType(supportCommunityRequest.getSupportCommunityType());
    }

    public long findSupportCommunityResponseCountBySupportCommunityType(SupportCommunityRequest supportCommunityRequest) {
        return supportCommunityRepository.findSupportCommunityResponseCountBySupportCommunityType(supportCommunityRequest.getSupportCommunityType());
    }

    public List<SupportCommunity> findSupportCommunityResponseBySupportCommunityTypeAndSearch(SupportCommunityRequest supportCommunityRequest , String search) {
        return supportCommunityRepository.findSupportCommunityResponseBySupportCommunityTypeAndSearch(supportCommunityRequest.getSupportCommunityType() , search);
    }

    public long findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(SupportCommunityRequest supportCommunityRequest , String search) {
        return supportCommunityRepository.findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(supportCommunityRequest.getSupportCommunityType() , search);
    }

    public SupportCommunity findSupportCommunityResponseById(long id) {
        SupportCommunity result = supportCommunityRepository.findById(id)
                .orElseThrow(() -> new SupportCommunityException("해당 게시물을 찾을 수 없습니다."));

        return result;
    }

}
