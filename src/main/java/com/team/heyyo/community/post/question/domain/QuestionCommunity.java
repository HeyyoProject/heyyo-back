package com.team.heyyo.community.post.question.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "question_community_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "question_community")
public class QuestionCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionCommunityId;

    private String title;

    @Lob
    private String content;

    private long userId;

    private boolean isSolved;

}