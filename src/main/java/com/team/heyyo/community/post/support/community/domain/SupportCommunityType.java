package com.team.heyyo.community.post.support.community.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SupportCommunityType {
    FREQUENTLY_ASKED_QUESTIONS,
    ANNOUNCEMENT;

    @JsonCreator
    public static SupportCommunityType from(String s) {
        return SupportCommunityType.valueOf(s.toUpperCase());
    }
}
