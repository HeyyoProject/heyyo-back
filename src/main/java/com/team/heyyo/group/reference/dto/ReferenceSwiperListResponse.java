package com.team.heyyo.group.reference.dto;

public record ReferenceSwiperListResponse(
    Long referenceId,
    String title,
    Boolean isScraped,
    String imageUrl
) {

    public static ReferenceSwiperListResponse of(
        Long referenceId,
        String title,
        Boolean isScraped,
        String imageUrl
    ) {
        return new ReferenceSwiperListResponse(
            referenceId,
            title,
            isScraped,
            imageUrl
        );
    }

}
