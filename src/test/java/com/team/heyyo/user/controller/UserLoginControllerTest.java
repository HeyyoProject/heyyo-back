package com.team.heyyo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserLoginRequest;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
class UserLoginControllerTest {


  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder = new BCryptPasswordEncoder();

  MockMvc mockMvc;

  @BeforeEach
  public void setup(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .build();
    userRepository.deleteAll();
  }

  final String USER_API = "/api/users";

  @DisplayName("로그인에 성공하면 accessToken과 refreshToken을 쿠키에 담아준다.")
  @Test
  void loginSuccess() throws Exception {
    //given
    final String api = USER_API + "/login";
    String email = "email";
    String password = "password";

    UserLoginRequest userLoginRequest = UserLoginRequest.of(email, password);

    User user = User.builder()
        .email(email)
        .password(encoder.encode(password))
        .build();
    userRepository.save(user);

    //when
    ResultActions resultActions = mockMvc.perform(
        post(api)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userLoginRequest))
    ).andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("user/login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("유저 이메일"),
                    fieldWithPath("password").description("유저 비밀번호")
                ),
                HeaderDocumentation.responseHeaders(
                    headerWithName("Authorization").description("accessToken")
                ),
                responseCookies(
                    cookieWithName("refresh_token").description("refreshToken")
                )
            )
        );

  }

  @DisplayName("로그인시 이메일이 존재하지 않으면 HTTP Status 404 리턴한다.")
  @Test
  void notExistsEmailReturn404() throws Exception {
    //given
    final String api = USER_API + "/login";
    String email = "email";
    String password = "password";

    UserLoginRequest userLoginRequest = UserLoginRequest.of(email, password);

    //when
    ResultActions resultActions = mockMvc.perform(
        post(api)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userLoginRequest))
    ).andDo(print());

    //then
    resultActions.andExpect(status().isNotFound())
        .andDo(document("user/login/notFound",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("유저 이메일"),
                    fieldWithPath("password").description("유저 비밀번호")
                ),
                responseFields(
                    fieldWithPath("message").description("메시지")
                )
            )
        );

  }

  @DisplayName("로그인시 이메일은 존재하는데 비밀번호를 틀리면 HTTP Status 400 리턴한다 ")
  @Test
  void wrongPassword() throws Exception {
    //given
    final String api = USER_API + "/login";
    String email = "email";
    String password = "password";
    String wrongPassword = "wrongPassword";

    UserLoginRequest userLoginRequest = UserLoginRequest.of(email, wrongPassword);

    User user = User.builder()
        .email(email)
        .password(encoder.encode(password))
        .build();
    userRepository.save(user);

    //when
    ResultActions resultActions = mockMvc.perform(
        post(api)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userLoginRequest))
    ).andDo(print());
    //then
    resultActions.andExpect(status().isBadRequest())
        .andDo(document("user/login/wrongPassword",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("유저 이메일"),
                    fieldWithPath("password").description("유저 비밀번호")
                ),
                responseFields(
                    fieldWithPath("message").description("메시지")
                )
            )
        );

  }
} 