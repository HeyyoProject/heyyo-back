package com.team.heyyo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserTypeRequest;
import com.team.heyyo.user.service.UserCharacterTypeService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
class UserCharacterTypeControllerTest {

  @Autowired
  ObjectMapper objectMapper;

  MockMvc mockMvc;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

  @MockBean
  UserCharacterTypeService userCharacterTypeService;


  final String USER_API = "/api/users";

  @DisplayName("캐릭터 타입을 저장하고 성공시 200을 반환한다.")
  @WithMockUser(roles = "USER")
  @Test
  void patchCharacterType() throws Exception {
    //given
    final String url = USER_API + "/character-types";

    UserTypeRequest userTypeRequest = UserTypeRequest.of(Mbti.Focus);
    String content = objectMapper.writeValueAsString(userTypeRequest);

    doReturn(true)
        .when(userCharacterTypeService).patchCharacterTypeWithAccessToken(any(UserTypeRequest.class), any());

    //when
    ResultActions resultActions = mockMvc.perform(patch(url)
            .with(csrf())
            .contentType(APPLICATION_JSON)
            .content(content)
            .header("Authorization", "Bearer accessToken"))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("user/character-type/patch/success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("mbti").description("유저 캐릭터 타입")
                ),
                HeaderDocumentation.requestHeaders(
                    headerWithName("Authorization").description("accessToken")
                ),
            responseFields(
                fieldWithPath("message").description("메시지")
            )
            )
        );

    UserBaseResponse response = objectMapper.readValue(
        resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
        UserBaseResponse.class
    );

    assertThat(response.getMessage()).isEqualTo("캐릭터 타입이 저장 되었습니다.");
  }

  @DisplayName("캐릭터 타입 저장에 실패시 400 을 리턴한다.")
  @WithMockUser(roles = "USER")
  @Test
  void failPatchCharacterType() throws Exception {
    //given
    final String url = USER_API + "/character-types";

    UserTypeRequest userTypeRequest = UserTypeRequest.of(Mbti.Focus);
    String content = objectMapper.writeValueAsString(userTypeRequest);

    doReturn(false)
        .when(userCharacterTypeService).patchCharacterTypeWithAccessToken(any(UserTypeRequest.class), any());


    //when
    ResultActions resultActions = mockMvc.perform(patch(url)
            .with(csrf())
            .contentType(APPLICATION_JSON)
            .content(content)
            .header("Authorization", "Bearer accessToken"))
        .andDo(print());

    //then
    resultActions.andExpect(status().isBadRequest())
        .andDo(document("user/character-type/patch/fail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("mbti").description("유저 캐릭터 타입")
                ),
                HeaderDocumentation.requestHeaders(
                    headerWithName("Authorization").description("accessToken")
                ),
                responseFields(
                    fieldWithPath("message").description("메시지")
                )
            )
        );

    UserBaseResponse response = objectMapper.readValue(
        resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
        UserBaseResponse.class
    );

    assertThat(response.getMessage()).isEqualTo("캐릭터 타입 저장에 실패 하였습니다.");
  }

}
