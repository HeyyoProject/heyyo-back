package com.team.heyyo.community.post.support.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.dto.CommentRequest;
import com.team.heyyo.community.post.support.community.dto.NewSupportCommunityRequest;
import com.team.heyyo.community.post.support.community.repository.SupportCommunityRepository;
import com.team.heyyo.community.post.support.community.service.SupportCommunityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class SupportCommunityServiceTest {

    @InjectMocks
    SupportCommunityService service;

    @Mock
    SupportCommunityRepository supportCommunityRepository;

    @Mock
    TokenProvider tokenProvider;

    @Test
    @DisplayName("고객센터 게시글 저장 ")
    public void saveSupportCommunity() {
        // given
        doReturn(1L).when(tokenProvider).getUserId("token");
        doReturn(null).when(supportCommunityRepository).save(any());
        NewSupportCommunityRequest request = NewSupportCommunityRequest.builder().tag(new String[]{}).build();

        // when
        Throwable throwable = catchThrowable(() -> service.saveSupportCommunity("token" , request));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("고객센터 Id로 조회 실패 _ 찾을 수 없는 아이디")
    public void findSupportCommunityResponseByIdFail_notFountId() {
        // given
        doReturn(Optional.empty()).when(supportCommunityRepository).findById(any());

        // when
        Throwable throwable = catchThrowable(() -> service.findSupportCommunityResponseById(40L));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 게시물을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("고객센터 Id로 조회 성공")
    public void findSupportCommunityResponseById() {
        // given
        doReturn(Optional.of(SupportCommunity.builder().build())).when(supportCommunityRepository).findById(any());

        // when
        Throwable throwable = catchThrowable(() -> service.findSupportCommunityResponseById(40L));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("고객센터 커멘트 작성 및 해결됨 표기 실패 _ 찾을 수 없는 게시물")
    public void updateCommentAndIsSolvedFail_NotFount() {
        // given
        doReturn(Optional.empty()).when(supportCommunityRepository).findById(any());

        // when
        Throwable throwable = catchThrowable(() -> service.updateCommentAndIsSolved(30L , CommentRequest.builder().message("message").build()));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 게시물을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("고객센터 커멘트 작성 및 해결됨 표기")
    public void updateCommentAndIsSolvedFail() {
        // given
        doReturn(Optional.of(SupportCommunity.builder().build())).when(supportCommunityRepository).findById(any());

        // when
        Throwable throwable = catchThrowable(() -> service.updateCommentAndIsSolved(30L , CommentRequest.builder().message("message").build()));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

}
