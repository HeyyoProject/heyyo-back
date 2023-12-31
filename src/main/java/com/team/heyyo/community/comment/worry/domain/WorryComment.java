package com.team.heyyo.community.comment.worry.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "worry_community_comment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "worry_comment")
public class WorryComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long worryCommentId;

    private Long worryCommunityId;

    @Lob
    private String content;

    @Column(name = "writer_user_id")
    private Long userId;

    @CreationTimestamp
    private Date writedDate;
}
