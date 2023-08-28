package com.team.heyyo.group.study.dto;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class GroupStudyListResponse {


    private String title;
    private List<String> tags;
    private int viewerCount;
    private boolean isLiked;

    public static GroupStudyListResponse of(String title, List<String> tags, int viewerCount, boolean isLiked) {
        return new GroupStudyListResponse(title, tags, viewerCount, isLiked);
    }
}
