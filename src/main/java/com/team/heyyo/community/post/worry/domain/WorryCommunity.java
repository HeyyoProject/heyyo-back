package com.team.heyyo.community.post.worry.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "worry_community_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "worry_community")
public class WorryCommunity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long worryCommunityId;

    private String title;

    @Lob
    private String content;

    private long userId;

    private boolean isSolved;

}