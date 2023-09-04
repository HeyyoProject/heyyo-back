package com.team.heyyo.community.post.support.tag.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "support_Tag_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "support_Tag")
public class SupportCommunityTagData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long supportTagKey;

    private String tag;

    public static SupportCommunityTagData buildTagData(String message) {
        return SupportCommunityTagData.builder()
                .tag(message)
                .build();
    }
}
