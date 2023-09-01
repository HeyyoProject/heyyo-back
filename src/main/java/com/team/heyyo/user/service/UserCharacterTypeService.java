package com.team.heyyo.user.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserTypeRequest;
import com.team.heyyo.user.exception.UserNotFoundException;
import com.team.heyyo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserCharacterTypeService {

  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;

  @Transactional
  public boolean patchCharacterTypeWithAccessToken(UserTypeRequest userTypeRequest,
      String accessToken) {
    long successCount = 1L;

    final Long userId = tokenProvider.getUserId(accessToken);
    User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

    user.updateCharacterType(userTypeRequest.mbti());

    return true;

  }
}
