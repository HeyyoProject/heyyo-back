package com.team.heyyo.group.reference.data.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "reference_scrap_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReferenceScrap {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceScrapId;

    private Long userId;

    private Long referenceId;

}
