package com.team.heyyo.community.post.support.community.dto;

import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupportCommunityRequest {
    SupportCommunityType supportCommunityType;

}
