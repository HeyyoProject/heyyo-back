package com.team.heyyo.group.reference.data.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "group_study_reference_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupStudyReferenceId;

    private long groupStudyId;

    @Column(name = "writer_user_key")
    private long userId;

    private String title;

    @Lob
    private String description;

    private String s3Url;

    @CreationTimestamp
    private Date writedDate;

}
