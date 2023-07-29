package com.team.heyyo.group.study.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "streaming_user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Streaming {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streamingId;

    private Long userId;

    private Long groupStudyId;

}
