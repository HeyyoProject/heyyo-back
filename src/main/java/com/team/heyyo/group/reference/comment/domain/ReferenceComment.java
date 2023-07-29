package com.team.heyyo.group.reference.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "reference_comment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReferenceComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long referenceCommentId;

    private long userId;

    @CreationTimestamp
    private Date writedDate;

    @Lob
    private String content;
}
