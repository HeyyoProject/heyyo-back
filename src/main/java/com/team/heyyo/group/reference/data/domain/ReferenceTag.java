package com.team.heyyo.group.reference.data.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "reference_tag_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReferenceTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long referenceTagId;

    private String tagData;
}