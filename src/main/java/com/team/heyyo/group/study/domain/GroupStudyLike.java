package com.team.heyyo.group.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "group_study_like_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GroupStudyLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupStudyLikeId;

    private Long userId;

    private Long groupStudyId;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public GroupStudyLike(Long userId, Long groupStudyId, LocalDateTime createdAt) {
        this.userId = userId;
        this.groupStudyId = groupStudyId;
        this.createdAt = createdAt;
    }
}
