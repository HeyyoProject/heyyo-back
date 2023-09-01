package com.team.heyyo.user.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserTypeRequest;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserCharacterTypeServiceTest {

  @InjectMocks
  UserCharacterTypeService userCharacterTypeService;

  @Mock
  TokenProvider tokenProvider;

  @Mock
  UserRepository userRepository;

  @DisplayName("유저의 캐릭터 타입을 저장 실패시 false를 반환한다.")
  @Test
  void failSaveCharacterType() {
      //given
    final Mbti mbti = Mbti.Focus;
    UserTypeRequest userTypeRequest = UserTypeRequest.of(mbti);
    final String accessToken = "accessToken";
    final Long userId = 1L;
    final Long notChanged = 0L;

    doReturn(userId).when(tokenProvider).getUserId(accessToken);
    doReturn(Optional.of(User.builder().build()))
            .when(userRepository).findByUserId(userId);

      //when
    boolean success = userCharacterTypeService.patchCharacterTypeWithAccessToken(userTypeRequest,
        accessToken);

      //then
    assertThat(success).isTrue();
  }
}