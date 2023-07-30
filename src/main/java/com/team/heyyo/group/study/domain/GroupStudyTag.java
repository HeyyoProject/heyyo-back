package com.team.heyyo.group.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "group_study_tag_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GroupStudyTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupStudyTagId;

    private Long groupStudyId;

    private String tagData;
}
