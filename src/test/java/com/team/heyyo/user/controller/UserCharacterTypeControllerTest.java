package com.team.heyyo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.user.constant.UserCharacterType;
import com.team.heyyo.user.dto.UserBaseResponse;
import com.team.heyyo.user.dto.UserTypeRequest;
import com.team.heyyo.user.service.UserCharacterTypeService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest(controllers = UserCharacterTypeController.class)
class UserCharacterTypeControllerTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserCharacterTypeService userCharacterTypeService;


  final String USER_API = "/api/users";

  @DisplayName("캐릭터 타입을 저장하고 성공시 200을 반환한다.")
  @WithMockUser(roles = "USER")
  @Test
  void patchCharacterType() throws Exception {
    //given
    final String url = USER_API + "/character-types";

    UserTypeRequest userTypeRequest = UserTypeRequest.of(UserCharacterType.고독);
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
                    fieldWithPath("userCharacterType").description("유저 캐릭터 타입")
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

    UserTypeRequest userTypeRequest = UserTypeRequest.of(UserCharacterType.고독);
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
                    fieldWithPath("userCharacterType").description("유저 캐릭터 타입")
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
