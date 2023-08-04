package com.team.heyyo.user.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.user.domain.User;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
class UserRegisterControllerTest {


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

  @DisplayName("회원가입 성공")
  @Test
  void registerSuccess() throws Exception {
    //given
    final String api = USER_API + "/register";
    UserRegisterRequest request = UserRegisterRequest.of("name", "test@email.com", "password",
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
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("phoneNumber").description("유저 핸드폰 번호"),
                fieldWithPath("recommendEmail").ignored()
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

    userRepository.save(
        User.builder()
            .email(duplicateEmail)
            .password("password")
            .name("name")
            .build()
    );

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
}