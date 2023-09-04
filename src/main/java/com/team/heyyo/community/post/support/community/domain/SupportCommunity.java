package com.team.heyyo.community.post.support.community.domain;

import com.team.heyyo.community.post.support.tag.domain.SupportCommunityTagData;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "support_community_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "support_community")
public class SupportCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportCommunityId;

    private String title;

    @Lob
    private String content;

    private Long userId;

    private String adminComment;

    private String isSolved;

    @Builder.Default
    @OneToMany(cascade = {CascadeType.PERSIST , CascadeType.REMOVE }, orphanRemoval = true)
    private List<SupportCommunityTagData> tagData = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SupportCommunityType supportCommunityType;

    public void addTagData(SupportCommunityTagData tagData) {
        this.tagData.add(tagData);
    }

}
