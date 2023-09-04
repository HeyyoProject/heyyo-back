package com.team.heyyo.community.post.support.community.dto;

import com.team.heyyo.community.post.support.tag.domain.SupportCommunityTagData;
import jakarta.persistence.Lob;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SupportCommunityResponse {
    private Long supportCommunityId;

    private String title;

    private String content;

    private String writer;

    private Object tag;
}
