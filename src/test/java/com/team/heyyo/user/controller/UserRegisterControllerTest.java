package com.team.heyyo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.sms.dto.SmsResponseDto;
import com.team.heyyo.sms.service.SmsService;
import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(controllers = UserRegisterController.class)
class UserRegisterControllerTest {


  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserService userService;

  @MockBean
  SmsService smsService;

  @BeforeEach
  public void init(WebApplicationContext webApplicationContext,
                   RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();
  }

  final String USER_API = "/api/users";

  @DisplayName("회원가입 성공")
  @Test
  void registerSuccess() throws Exception {
    //given
    final String api = USER_API + "/register";
    UserRegisterRequest request = UserRegisterRequest.of("name", "nickname", "test@email.com", "password",
        "01012341234");


    //when
    final ResultActions resultActions = mockMvc.perform(
            post(api)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
    ).andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("user/register",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("name").description("유저 성함"),
                fieldWithPath("nickname").description("유저 닉네임"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("phoneNumber").description("유저 핸드폰 번호"),
                fieldWithPath("recommendNickname").ignored()
            ),
            responseFields(
                fieldWithPath("message").description("메시지")
            )
        ));

  }

  @DisplayName("이메일 중복확인 성공")
  @Test
  void validateEmailSuccess() throws Exception {
    //given
    final String api = USER_API + "/duplicate/emails/{email}";
    final String email = "test@email.com";

    doReturn(UserResponseCode.SUCCESS)
            .when(userService).isEmailDuplicate(email);
    //when
    final ResultActions resultActions = mockMvc.perform(
        get(api, email)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("user/duplicate/email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("email").description("중복확인할 이메일")
            ),
            responseFields(
                fieldWithPath("message").description("메시지")
            )
        ));
  }

  @DisplayName("이메일 중복확인 실패- 중복된 이메일 HttpStatus 400 리턴")
  @Test
  void duplicateEmail() throws Exception {
    //given
    final String api = USER_API + "/duplicate/emails/{email}";
    final String duplicateEmail = "duplicateEmail@email.com";

    doReturn(UserResponseCode.EMAIL_DUPLICATION)
            .when(userService).isEmailDuplicate(duplicateEmail);
    //when
    final ResultActions resultActions = mockMvc.perform(
        get(api, duplicateEmail)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isBadRequest())
        .andDo(document("user/duplicate/email/fail",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("email").description("중복확인할 이메일")
            ),
            responseFields(
                fieldWithPath("message").description("메시지")
            )
        ));
  }

  @DisplayName("닉네임 중복확인 성공")
  @Test
  void validateNicknameSuccess() throws Exception {
    //given
    final String api = USER_API + "/duplicate/nicknames/{nickname}";
    final String nickname = "nickname";

    doReturn(UserResponseCode.SUCCESS)
            .when(userService).isNicknameDuplicate(nickname);
    //when
    final ResultActions resultActions = mockMvc.perform(
            get(api, nickname)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isOk())
            .andDo(document("user/duplicate/nickname/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("nickname").description("중복확인할 닉네임")
                    ),
                    responseFields(
                            fieldWithPath("message").description("메시지")
                    )
            ));

    UserBaseResponse response = objectMapper.readValue(
            resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
            UserBaseResponse.class
    );
    assertThat(response.message()).isEqualTo("성공");

  }

  @DisplayName("닉네임 중복확인 실패")
  @WithMockUser(roles = "USER")
  @Test
  void duplicateNickname() throws Exception {
    //given
    final String api = USER_API + "/duplicate/nicknames/{nickname}";
    final String duplicateNickname = "duplicateNickname";

    doReturn(UserResponseCode.NICKNAME_DUPLICATION)
            .when(userService).isNicknameDuplicate(duplicateNickname);
    //when
    final ResultActions resultActions = mockMvc.perform(
            get(api, duplicateNickname)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isBadRequest())
            .andDo(document("user/duplicate/nickname/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("nickname").description("중복확인할 닉네임")
                    ),
                    responseFields(
                            fieldWithPath("message").description("메시지")
                    )
            ));

    UserBaseResponse response = objectMapper.readValue(
            resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
            UserBaseResponse.class
    );
    assertThat(response.message()).isEqualTo("사용 불가능한 닉네임입니다.");

  }

  @DisplayName("전화번호로 메시지 전송하기")
  @Test
  void sendSmsMessage() throws Exception {
    //given
    final String api = USER_API + "/sms/{to}";
    final String phone = "01012341234";
    int certificationNumber = 123123;

    SmsResponseDto success = SmsResponseDto.builder()
            .statusCode("202")
            .statusName("success")
            .requestId("requestId")
            .requestTime(LocalDateTime.now())
            .certificationNumber(certificationNumber)
            .build();

    doReturn(success)
            .when(smsService).sendSmsToUserWithCertificationNumber(phone);
    //when
    ResultActions resultActions = mockMvc.perform(
            get(api, phone)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isAccepted())
            .andDo(document("user/sms/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("to").description("sms 메시지를 보낼 사용자 휴대전화")
                    ),
                    responseFields(
                            fieldWithPath("requestId").description("requestId"),
                            fieldWithPath("requestTime").description("응답 시간"),
                            fieldWithPath("statusCode").description("Http 응답 번호"),
                            fieldWithPath("statusName").description("Http 응답 이름"),
                            fieldWithPath("certificationNumber").description("인증번호")
                    )
            ));

  }

  @DisplayName("전화번호로 메시지 전송하기 실패 (sms 서버 에러)")
  @Test
  void failSendSmsMessage() throws Exception {
    //given
    final String api = USER_API + "/sms/{to}";
    final String phone = "01012341234";

    SmsResponseDto fail = SmsResponseDto.builder()
            .statusCode("404")
            .build();

    doReturn(fail)
            .when(smsService).sendSmsToUserWithCertificationNumber(phone);
    //when
    ResultActions resultActions = mockMvc.perform(
            get(api, phone)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isNotFound())
            .andDo(document("user/sms/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("to").description("sms 메시지를 보낼 사용자 휴대전화")
                    ),
                    responseFields(
                            fieldWithPath("statusCode").description("Http 응답 번호"),
                            fieldWithPath("requestId").description("requestId").ignored(),
                            fieldWithPath("requestTime").description("응답 시간").ignored(),
                            fieldWithPath("statusName").description("Http 응답 이름").ignored(),
                            fieldWithPath("certificationNumber").description("인증번호").ignored()
                    )
            ));

  }
}