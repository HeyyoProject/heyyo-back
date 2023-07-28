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
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentKey;

    private long questionCommunityKey;

    @Column(name = "writer_user_key")
    private long userKey;

    @Lob
    private String content;

    @CreationTimestamp
    private Date writedDate;
}
