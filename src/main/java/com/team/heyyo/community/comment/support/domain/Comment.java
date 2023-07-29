package com.team.heyyo.community.comment.support.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "support_community_comment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "support_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    private long supportCommunityId;

    @Column(name = "writer_user_key")
    private long userId;

    @Lob
    private String content;

    @CreationTimestamp
    private Date writedDate;
}
