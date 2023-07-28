package com.team.heyyo.community.post.question.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "question_community_tag_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CommunityTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long communityTagKey;

    private long questionCommunityKey;

    private String tagData;
}
