package com.team.heyyo.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.user.domain.User;
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
class UserFindControllerTest {

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

  @DisplayName("비밀번호 찾기 성공시 임시 비밀번호를 해당 이메일로 보내준다")
  @Test
  void findPassword() throws Exception {
    //given
    final String api = USER_API + "/passwords/{name}/{email}";
    final String email = "pica23000@naver.com";
    final String name = "박재완";

    userRepository.save(User.builder()
        .email(email)
        .password("testpassword")
        .name(name)
        .build()
    );

    //when
    ResultActions resultActions = mockMvc.perform(
        get(api, name, email)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("user/findPassword/success",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("name").description("이름"),
                parameterWithName("email").description("이메일")
            ),
            responseFields(
                fieldWithPath("message").description("메시지")
            )
        ));
  }

  @DisplayName("비밀번호 찾기 실패 - 존재하지않는 회원")
  @Test
  void cantFindEmail() throws Exception {
    //given
    final String api = USER_API + "/passwords/{name}/{email}";
    final String email = "pica23000@naver.com";
    final String name = "박재완";
    final String wrongEmail = "test@email.com";

    userRepository.save(User.builder()
        .email(email)
        .password("testpassword")
        .name(name)
        .build()
    );

    //when
    ResultActions resultActions = mockMvc.perform(
        get(api, name, wrongEmail)
    ).andDo(print());

    //then
    resultActions.andExpect(status().isNotFound())
        .andDo(document("user/findPassword/userNotFound",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("name").description("이름"),
                parameterWithName("email").description("이메일")
            ),
            responseFields(
                fieldWithPath("message").description("메시지")
            )
        ));
  }
}