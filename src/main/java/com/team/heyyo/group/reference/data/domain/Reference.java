package com.team.heyyo.group.reference.data.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "group_study_reference_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupStudyReferenceId;

    private Long groupStudyId;

    @Column(name = "writer_user_id")
    private Long userId;

    private String title;

    @Lob
    private String description;

    private Integer views;

    private String s3Url;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Reference(Long groupStudyId, Long userId, String title, String description, Integer views, String s3Url) {
        this.groupStudyId = groupStudyId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.s3Url = s3Url;
        this.createdAt = LocalDateTime.now();
    }

}
