package com.team.heyyo.group.study.dto;

import lombok.*;

import java.util.List;
//FIXME : 좋아요 수 리턴하는 로직 추가 구현 필요
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

    public static GroupStudyListResponse of(String title, List<String> tags, int viewerCount) {
        return new GroupStudyListResponse(title, tags, viewerCount, true);
    }

}
