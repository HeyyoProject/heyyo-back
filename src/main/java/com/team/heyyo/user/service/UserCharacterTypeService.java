package com.team.heyyo.user.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.user.dto.UserTypeRequest;
import com.team.heyyo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCharacterTypeService {

  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;

  public boolean patchCharacterTypeWithAccessToken(UserTypeRequest userTypeRequest,
      String accessToken) {
    long successCount = 1L;

    final Long userId = tokenProvider.getUserId(accessToken);
    Long resultCount = userRepository.updateCharacterTypeWithUserId(userTypeRequest.userCharacterType(), userId);

    return resultCount.equals(successCount);
  }
}
