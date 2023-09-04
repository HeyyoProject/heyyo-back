package com.team.heyyo.community.post.support.community.repository;

import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;

import java.util.List;

public interface CustomSupportCommunity {

    List<SupportCommunity> findSupportCommunityResponseBySupportCommunityType(SupportCommunityType supportCommunityType);

    long findSupportCommunityResponseCountBySupportCommunityType(SupportCommunityType supportCommunityType);

    List<SupportCommunity> findSupportCommunityResponseById(long id);

    List<SupportCommunity> findSupportCommunityResponseBySupportCommunityTypeAndSearch(SupportCommunityType supportCommunityType , String search);

    long findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(SupportCommunityType supportCommunityType , String search);

}
