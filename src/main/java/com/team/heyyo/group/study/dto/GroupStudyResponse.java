package com.team.heyyo.group.study.dto;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class GroupStudyResponse{


    private String title;
    private List<String> tags;
    private int viewerCount;
    private boolean isLiked;

    public static GroupStudyResponse of(String title, List<String> tags, int viewerCount, boolean isLiked) {
        return new GroupStudyResponse(title, tags, viewerCount, isLiked);
    }
}
