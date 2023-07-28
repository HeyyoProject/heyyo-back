package com.team.heyyo.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Mbti {
    Loneliness("고독해요") ,
    Communication("소통해요"),
    Crowded("북적해요"),
    Quiet("한산해요"),
    Researching("연구해요"),
    Useful("유익해요"),
    Timid("소심해요"),
    Focus("집중해요");

    private final String description;
}
