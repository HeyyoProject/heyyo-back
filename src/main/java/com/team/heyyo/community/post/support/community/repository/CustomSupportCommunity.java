package com.team.heyyo.community.post.support.community.repository;

import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomSupportCommunity {

    List<SupportCommunity> findSupportCommunityResponseBySupportCommunityType(Pageable pageable , SupportCommunityType supportCommunityType);

    long findSupportCommunityResponseCountBySupportCommunityType(SupportCommunityType supportCommunityType);

    List<SupportCommunity> findSupportCommunityResponseBySupportCommunityTypeAndSearch(Pageable pageable , SupportCommunityType supportCommunityType , String search);

    long findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(SupportCommunityType supportCommunityType , String search);

}
