package com.team.heyyo.community.post.worry.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "worry_community_tag_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "worry_community_tag")
public class CommunityTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long communityTagKey;

    private long worryCommunityKey;

    private String tagData;
}
