package com.team.heyyo.community.post.support.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "support_community_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "support_community")
public class SupportCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long supportCommunityId;

    private String title;

    @Lob
    private String content;

    private long userId;

}