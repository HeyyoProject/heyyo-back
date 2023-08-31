package com.team.heyyo.group.reference.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.group.reference.constant.ReferenceOrderBy;
import com.team.heyyo.group.reference.data.domain.Reference;
import com.team.heyyo.group.reference.data.domain.ReferenceTag;
import com.team.heyyo.group.reference.dto.ReferenceListResponse;
import com.team.heyyo.group.reference.dto.ReferenceSwiperListResponse;
import com.team.heyyo.group.reference.repository.reference.ReferenceRepository;
import com.team.heyyo.group.reference.repository.referenceScrap.ReferenceScrapRepository;
import com.team.heyyo.group.reference.repository.referenceTag.ReferenceTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReferenceService {

    private final ReferenceRepository referenceRepository;
    private final ReferenceScrapRepository referenceScrapRepository;
    private final ReferenceTagRepository referenceTagRepository;

    private final TokenProvider tokenProvider;

    public List<ReferenceSwiperListResponse> findTodayMostLikeReference(String accessToken) {

        Long userId = tokenProvider.getUserId(accessToken);

        List<Reference> referenceList = referenceRepository.findReferenceTodayMostLike();

        List<ReferenceSwiperListResponse> referenceResponseList = new ArrayList<>();
        for (Reference reference : referenceList) {
            boolean isLiked = referenceScrapRepository.findByReferenceIdAndUserId(reference.getGroupStudyReferenceId(), userId).isPresent();

            referenceResponseList.add(ReferenceSwiperListResponse.of(reference.getGroupStudyReferenceId(), reference.getTitle(), isLiked, reference.getS3Url()));
        }

        return referenceResponseList;
    }

    public List<ReferenceListResponse> findReferenceList(ReferenceOrderBy orderBy, Pageable pageable, String accessToken) {

        Long userId = tokenProvider.getUserId(accessToken);

        List<Reference> referenceList = null;

        switch (orderBy) {
            case POPULAR: referenceList = referenceRepository.findAllOrderByScrapDesc(pageable);
            break;

            case VIEWS: referenceList = referenceRepository.findAllOrderByViewsDesc(pageable);
            break;

            case RECENT: referenceList = referenceRepository.findAllOrderByCreatedAtDesc(pageable);
        }

        List<ReferenceListResponse> referenceResponseList = new ArrayList<>();

        for (Reference reference : referenceList) {
            List<ReferenceTag> referenceTagList = referenceTagRepository.findByReferenceId(reference.getGroupStudyReferenceId());
            List<String> categories = new ArrayList<>();
            for (ReferenceTag referenceTag : referenceTagList) {
                categories.add(referenceTag.getTagData());
            }
            boolean isScraped = referenceScrapRepository.existsByReferenceIdAndUserId(reference.getGroupStudyReferenceId(), userId);
            ReferenceListResponse response = ReferenceListResponse
                    .of(reference.getGroupStudyReferenceId(), reference.getTitle(), reference.getDescription(), categories, isScraped, reference.getS3Url());
            referenceResponseList.add(response);
        }

            return referenceResponseList;
    }
}
