package com.team.heyyo.group.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "group_study_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GroupStudy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupStudyKey;

    @Column(name = "owner_user_key")
    private long userKey;

    private String title;

    private String description;

    private String session;

}
