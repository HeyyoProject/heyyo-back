package com.team.heyyo.group.reference.dto;

import java.util.List;

public record ReferenceListResponse(
        Long referenceId,
        String title,
        String description,
        List<String> tags,
        Boolean isScraped,
        String imageUrl
) {

    public static ReferenceListResponse of(
            Long referenceId,
            String title,
            String description,
            List<String> tags,
            Boolean isScraped,
            String imageUrl
    ) {
        return new ReferenceListResponse(
                referenceId,
                title,
                description,
                tags,
                isScraped,
                imageUrl
        );
    }
}
