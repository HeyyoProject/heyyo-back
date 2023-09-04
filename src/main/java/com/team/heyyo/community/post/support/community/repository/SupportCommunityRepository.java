package com.team.heyyo.community.post.support.community.repository;

import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportCommunityRepository extends JpaRepository<SupportCommunity , Long> , CustomSupportCommunity {

}
