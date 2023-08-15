package com.team.heyyo.user.service;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    PasswordEncoder encoder = new BCryptPasswordEncoder();


    @DisplayName("유저의 정보를 저장한다.")
    @Test
    void register() {
        //given
        final String encodedPassword = "string";

        UserRegisterRequest request = UserRegisterRequest.of(
                "name",
                "nickname",
                "email",
                "password",
                "phoneNumber"
        );

        doReturn(User.builder().build()).when(userRepository).save(any());
        doReturn(encodedPassword).when(passwordEncoder).encode(anyString());
        //when
        boolean isRegistered = userService.register(request);

        //then
        assertThat(isRegistered).isTrue();
    }

    @DisplayName("로그인 시 회원의 아이디와 비밀번호가 일치하면 true를 리턴한다.")
    @Test
    void isEmailAndPasswordCorrect() {
        //given
        final String email = "test@email.com";
        final String password = "password";
        Optional<User> optionalUser = Optional.of(
                User.builder()
                    .password(encoder.encode(password))
                    .build()
        );
        doReturn(optionalUser).when(userRepository).findByEmail(anyString());

        //when
        UserResponseCode userResponseCode = userService.isEmailAndPasswordCorrect(email, password);

        //then
        assertThat(userResponseCode).isEqualTo(UserResponseCode.SUCCESS);
    }

    @DisplayName("회원의 이메일이 올바르지 않으면 에러를 던진다")
    @Test
    void emailNotFound() {
        //given
        final String wrongEmail = "wrongEmail";
        final String rightEmail = "rightEmail";
        final String password = "password";

        doReturn(Optional.empty()).when(userRepository).findByEmail(anyString());

        //when
        UserResponseCode userResponseCode = userService.isEmailAndPasswordCorrect(rightEmail, password);

        //then
        assertThat(userResponseCode).isEqualTo(UserResponseCode.EMAIL_NOT_FOUND);
    }

    @DisplayName("회원의 비밀번호가 올바르지 않으면 에러를 던진다")
    @Test
    void passwordNotMatch() {
        //given
        final String email = "email";
        final String password = "password";
        final String wrongPassword = "wrongPassword";

        Optional<User> optionalUser = Optional.of(
                User.builder()
                        .password(encoder.encode(password))
                        .build()
        );
        doReturn(optionalUser).when(userRepository).findByEmail(anyString());

        //when
        UserResponseCode userResponseCode = userService.isEmailAndPasswordCorrect(email, wrongPassword);

        //then
        assertThat(userResponseCode).isEqualTo(UserResponseCode.PASSWORD_NOT_MATCH);
    }

    @DisplayName("닉네임이 저장되어있으면 UserResponseCode SUCCESS를 반환한다.")
    @Test
    void isNicknameDuplicate() {
        //given
        final String duplicateNickname = "nickname";

        doReturn(Optional.empty())
                .when(userRepository).findByNickname(duplicateNickname);
        //when
        UserResponseCode successResponseCode = userService.isNicknameDuplicate(duplicateNickname);

        //then
        assertThat(successResponseCode).isEqualTo(UserResponseCode.SUCCESS);
    }

    @DisplayName("닉네임이 저장되어있으면 UserResponseCode NICKNAME_DUPLICATION을 반환한다.")
    @Test
    void isNicknameNotDuplicate() {
        //given
        final String duplicateNickname = "nickname";

        doReturn(Optional.of(User.builder().build()))
                .when(userRepository).findByNickname(duplicateNickname);
        //when
        UserResponseCode successResponseCode = userService.isNicknameDuplicate(duplicateNickname);

        //then
        assertThat(successResponseCode).isEqualTo(UserResponseCode.NICKNAME_DUPLICATION);
    }

}