package com.team.heyyo.user.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserModifyRequest;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.dto.UserResponse;
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

import javax.security.auth.login.AccountException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    TokenProvider tokenProvider;

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

    @Test
    @DisplayName("사용자 ID 로 password 바꾸기 실패 _ 사용자 정보를 찾을 수 없음.")
    void modifyPasswordByUserIdFail_notFoundUser() {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.empty()).when(userRepository).findById(0L);
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("data").checkPassword("password").build();

        // when
        Throwable throwable = catchThrowable(() -> userService.updatePasswordByToken("token" , userModifyRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 id와 일치하는 사용자가 없습니다.");
    }

    @Test
    @DisplayName("사용자 ID 로 password 바꾸기 실패 _ 사용자 비밀번호가 일치하지 않음.")
    void modifyPasswordByUserIdFail_not() {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(createUser())).when(userRepository).findById(0L);
        doReturn(false).when(passwordEncoder).matches(anyString() , anyString());
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("data").checkPassword("password").build();

        // when
        Throwable throwable = catchThrowable(() -> userService.updatePasswordByToken("token" , userModifyRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("사용자 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("사용자 ID 로 password 바꾸기")
    void modifyPasswordByUserId() {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(createUser())).when(userRepository).findById(0L);
        doReturn(true).when(passwordEncoder).matches(anyString() , anyString());
        doReturn(null).when(userRepository).updatePasswordWithId("newPassword" , 0L);
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("newPassword").checkPassword("password").build();

        // when
        Throwable throwable = catchThrowable(() -> userService.updatePasswordByToken("token" , userModifyRequest));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용자 ID 로 닉네임 바꾸기 실패 _ 사용자를 찾을 수 없음.")
    void modifyNickNameByUserIdFail_NotFoundUser() {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.empty()).when(userRepository).findById(0L);
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("newNickname").checkPassword("password").build();

        // when
        Throwable throwable = catchThrowable(() -> userService.updateNickNameByToken("token" , userModifyRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 id와 일치하는 사용자가 없습니다.");
    }

    @Test
    @DisplayName("사용자 ID 로 닉네임 바꾸기 실패 _ 비밀 번호가 일치하지 않습니다.")
    void modifyNickNameByUserIdFail_NotEqualPassword() {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(createUser())).when(userRepository).findById(0L);
        doReturn(false).when(passwordEncoder).matches(anyString() , anyString());
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("newNickname").checkPassword("password").build();

        // when
        Throwable throwable = catchThrowable(() -> userService.updateNickNameByToken("token" , userModifyRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("사용자 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("사용자 ID 로 닉네임 바꾸기 실패 _ 이미 사용중인 닉네임입니다")
    void modifyNickNameByUserIdFail_ExistsNickName() throws AccountException {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(createUser())).when(userRepository).findById(0L);
        doReturn(true).when(passwordEncoder).matches(anyString() , anyString());
        doReturn(Optional.of(createUser())).when(userRepository).findByNickname("newNickname");
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("newNickname").checkPassword("password").build();

        // when
        Throwable throwable = catchThrowable(() -> userService.updateNickNameByToken("token" , userModifyRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("이미 사용중인 닉네임입니다.");
    }

    @Test
    @DisplayName("사용자 ID 로 닉네임 바꾸기")
    void modifyNickNameByUserId() throws AccountException {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(createUser())).when(userRepository).findById(0L);
        doReturn(true).when(passwordEncoder).matches(anyString() , anyString());
        doReturn(1L).when(userRepository).updateNickNameWithId("newNickname" , 0L);
        doReturn(Optional.empty()).when(userRepository).findByNickname("newNickname");
        UserModifyRequest userModifyRequest = UserModifyRequest.builder().data("newNickname").checkPassword("password").build();

        // when
        TodoListMessageResponse result = userService.updateNickNameByToken("token" , userModifyRequest);

        // then
        assertThat(result.getMessage()).isEqualTo("닉네임이 변경되었습니다.");
    }

    @Test
    @DisplayName("유저 정보를 가져오기 실패 _ 사용자 정보 탐색 실패")
    void getUserByTokenFail_NotFountUser() {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.empty()).when(userRepository).findById(0L);

        // when
        Throwable throwable = catchThrowable(() -> userService.getUserByToken("token"));

        // then
        assertThat(throwable.getMessage()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("유저 정보를 가져오기")
    void getUserByToken() throws AccountException {
        // given
        doReturn(0L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(createUser())).when(userRepository).findById(0L);

        // when
        UserResponse userResponse = userService.getUserByToken("token");

        // then
        assertThat(userResponse.getName()).isEqualTo("name");
    }

    public User createUser() {
        return User.builder()
                .email("email")
                .name("name")
                .password("password")
                .nickname("nickNames")
                .role(UserRole.USER)
                .build();
    }

}