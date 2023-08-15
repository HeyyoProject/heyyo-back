package com.team.heyyo.user.service;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.exception.UserNotFoundException;
import com.team.heyyo.user.handler.UserLoginHandler;
import com.team.heyyo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserLoginHandler loginHandler;

  @Transactional
  public boolean register(UserRegisterRequest request) {
    userRepository.save(
        User.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(UserRole.USER)
                .build()
    );
    return true;
  }

  public User findById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("해당 id와 일치하는 사용자가 없습니다."));
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("해당 email과 일치하는 user가 없습니다."));
  }


  public UserResponseCode isEmailAndPasswordCorrect(String email, String requestPassword) {
    return userRepository.findByEmail(email)
        .map(user -> passwordEncoder.matches(requestPassword, user.getPassword())
            ? UserResponseCode.SUCCESS
            : UserResponseCode.PASSWORD_NOT_MATCH)
        .orElse(UserResponseCode.EMAIL_NOT_FOUND);
  }


  public void setTokensIfEmailAndPasswordCorrect(UserResponseCode responseCode, String email,
      HttpServletRequest request, HttpServletResponse response) {
    if (responseCode.name().equals(UserResponseCode.SUCCESS.name())) {
      setTokens(email, request, response);
    }
  }

  private void setTokens(String email, HttpServletRequest request, HttpServletResponse response) {
    User user = findByEmail(email);
    loginHandler.onAuthenticationSuccess(user, request, response);
  }

  public UserResponseCode isEmailDuplicate(String email) {
    return userRepository.findByEmail(email)
        .map(user -> UserResponseCode.EMAIL_DUPLICATION)
        .orElse(UserResponseCode.SUCCESS);
  }

  public UserResponseCode findPasswordWithEmailAndName(String email, String name) {
    return userRepository.findUserByEmailAndName(email, name)
        .map(user -> UserResponseCode.SUCCESS)
        .orElse(UserResponseCode.NAME_PASSWORD_NOT_MATCH);
  }

  @Transactional
  public Long updatePassword(String email, String randomPassword) {
    return userRepository.findByEmail(email)
        .map(user ->
            userRepository.updatePasswordWithEmail(
                passwordEncoder.encode(randomPassword), email))
        .orElseThrow(() -> new UserNotFoundException("해당 email과 일치하는 사용자가 없습니다."));
  }

  public UserResponseCode isNicknameDuplicate(String nickname) {
    return userRepository.findByNickname(nickname)
            .map(user -> UserResponseCode.NICKNAME_DUPLICATION)
            .orElse(UserResponseCode.SUCCESS);
  }
}
