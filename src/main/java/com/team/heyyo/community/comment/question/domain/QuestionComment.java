package com.team.heyyo.community.comment.question.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "question_community_comment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "question_comment")
public class QuestionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionCommentId;

    private Long questionCommunityId;

    @Column(name = "writer_user_key")
    private Long userId;

    @Lob
    private String content;

    @CreationTimestamp
    private Date writedDate;

}
