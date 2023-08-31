package com.team.heyyo.group.reference.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.group.reference.data.domain.Reference;
import com.team.heyyo.group.reference.dto.ReferenceSwiperListResponse;
import com.team.heyyo.group.reference.repository.reference.ReferenceRepository;
import com.team.heyyo.group.reference.repository.referenceScrap.ReferenceScrapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferenceServiceTest {

    @InjectMocks
    ReferenceService referenceService;

    @Mock
    ReferenceRepository referenceRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    ReferenceScrapRepository referenceScrapRepository;

    @DisplayName("오늘 가장 좋아요를 많이 받은 레퍼런스를 조회한다.")
    @Test
    void todayLIkeReferenceList() {
        //given
        final String accessToken = "accessToken";

        Reference reference1 = Reference.builder()
                .title("title")
                .s3Url("content")
                .build();
        Reference reference2 = Reference.builder()
                .title("title")
                .s3Url("content")
                .build();

        when(tokenProvider.getUserId(any()))
                .thenReturn(1L);
        when(referenceRepository.findReferenceTodayMostLike())
                .thenReturn(List.of(reference1, reference2));

        //when
        List<ReferenceSwiperListResponse> referenceListResponse = referenceService.findTodayMostLikeReference(accessToken);

        //then
        assertThat(referenceListResponse).hasSize(2);
    }


}