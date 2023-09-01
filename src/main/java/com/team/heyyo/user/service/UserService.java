package com.team.heyyo.user.service;

import com.team.heyyo.auth.jwt.constant.JwtTokenConstant;
import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserModifyRequest;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.dto.UserResponse;
import com.team.heyyo.user.exception.UserNotFoundException;
import com.team.heyyo.user.handler.UserLoginHandler;
import com.team.heyyo.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserLoginHandler loginHandler;
  private final TokenProvider tokenProvider;

  @Transactional
  public boolean register(UserRegisterRequest request) {
    userRepository.save(
        User.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhoneNumber())
                .role(UserRole.USER)
                .mbti(Mbti.Communication)
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
  public Long updatePassword(String email, String password) {
    return userRepository.findByEmail(email)
        .map(user ->
            userRepository.updatePasswordWithEmail(
                passwordEncoder.encode(password), email))
        .orElseThrow(() -> new UserNotFoundException("해당 email과 일치하는 사용자가 없습니다."));
  }

  public UserResponseCode isNicknameDuplicate(String nickname) {
    return userRepository.findByNickname(nickname)
            .map(user -> UserResponseCode.NICKNAME_DUPLICATION)
            .orElse(UserResponseCode.SUCCESS);
  }

  public void isValidViaPassword(Long userId , String password) throws AccountException {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("해당 id와 일치하는 사용자가 없습니다."));

    if(!passwordEncoder.matches(user.getPassword() , password)) {
      throw new AccountException("사용자 비밀번호가 일치하지 않습니다.");
    }
  }

  public TodoListMessageResponse updatePasswordByToken(String token , UserModifyRequest userModifyRequest) throws AccountException {
    long userId = tokenProvider.getUserId(token);
    isValidViaPassword(userId , userModifyRequest.getCheckPassword());

    userRepository.updatePasswordWithId(userModifyRequest.getData() , userId);
    return TodoListMessageResponse.of("비밀번호가 변경되었습니다.");
  }

  public TodoListMessageResponse updateNickNameByToken(String token , UserModifyRequest userModifyRequest) throws AccountException {
    long userId = tokenProvider.getUserId(token);
    String nickName = userModifyRequest.getData();
    isValidViaPassword(userId , userModifyRequest.getCheckPassword());

    if(isNicknameDuplicate(nickName).getStatus() == UserResponseCode.NICKNAME_DUPLICATION.getStatus()) {
      throw new AccountException("이미 사용중인 닉네임입니다.");
    }

    userRepository.updateNickNameWithId(nickName , userId);
    return TodoListMessageResponse.of("닉네임이 변경되었습니다.");
  }

  public UserResponse getUserByToken(String token) throws AccountException {
    long userId = tokenProvider.getUserId(token);
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new AccountException("사용자 정보를 찾을 수 없습니다."));

    return UserResponse.createUserResponse(user);
  }

  public void deleteRefreshTokenCookie(HttpServletResponse response) {
    Cookie refreshTokenCookie = new Cookie(JwtTokenConstant.REFRESH_TOKEN.getName(), null);
    refreshTokenCookie.setMaxAge(0);
    refreshTokenCookie.setPath("/");

    response.addCookie(refreshTokenCookie);
  }
}
