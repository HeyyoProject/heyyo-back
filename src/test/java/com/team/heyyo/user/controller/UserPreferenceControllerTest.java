package com.team.heyyo.user.controller;

import com.google.gson.Gson;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserModifyRequest;
import com.team.heyyo.user.dto.UserResponse;
import com.team.heyyo.user.exception.UserNotFoundException;
import com.team.heyyo.user.service.UserService;
import com.team.heyyo.util.GsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.security.auth.login.AccountException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
public class UserPreferenceControllerTest {

    @MockBean
    UserService userService;

    Gson gson;

    MockMvc mockMvc;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        gson = GsonUtil.getGsonInstance();
    }

    @DisplayName("userId로 password 변경 실패 _ 사용자 정보를 찾을 수 없음")
    @Test
    void modifyPasswordByUserIdFail_notFountData() throws Exception {
        //given
        final String api = "/api/users/password";
        UserModifyRequest userRequest = UserModifyRequest.builder().checkPassword("checkPassword").data("newPassword").build();
        doThrow(new UserNotFoundException("해당 id와 일치하는 사용자가 없습니다.")).when(userService).updatePasswordByToken(any(), any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/preference/dummyData",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("data").description("변경할 비밀번호"),
                                        fieldWithPath("checkPassword").description("비밀번호 확인")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("해당 id와 일치하는 사용자가 없습니다.");
    }

    @DisplayName("userId로 password 변경 실패 _ 사용자 비밀번호가 일치하지 않습니다.")
    @Test
    void modifyPasswordByUserIdFail_notEqualPassword() throws Exception {
        //given
        final String api = "/api/users/password";
        UserModifyRequest userRequest = UserModifyRequest.builder().checkPassword("checkPassword").data("newPassword").build();
        doThrow(new AccountException("사용자 비밀번호가 일치하지 않습니다.")).when(userService).updatePasswordByToken(any(), any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/preference/dummyData",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("data").description("변경할 비밀번호"),
                                        fieldWithPath("checkPassword").description("비밀번호 확인")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("사용자 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("userId로 password 변경")
    @Test
    void modifyPasswordByUserId() throws Exception {
        //given
        final String api = "/api/users/password";
        UserModifyRequest userRequest = UserModifyRequest.builder().checkPassword("checkPassword").data("newPassword").build();
        doReturn(TodoListMessageResponse.of("비밀번호가 변경되었습니다.")).when(userService).updatePasswordByToken(any(), any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("user/preference/modifyPassword",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("data").description("변경할 비밀번호"),
                                        fieldWithPath("checkPassword").description("비밀번호 확인")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("비밀번호가 변경되었습니다.");
    }

    @DisplayName("userId로 닉네임 변경 실패 _ 이미 사용중인 닉네임")
    @Test
    void modifyNicknameByUserIdFail_ExistsNickName() throws Exception {
        //given
        final String api = "/api/users/nickname";
        UserModifyRequest userRequest = UserModifyRequest.builder().checkPassword("checkPassword").data("newPassword").build();
        doThrow(new AccountException("이미 사용중인 닉네임입니다.")).when(userService).updateNickNameByToken(any(), any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/preference/dummyData",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("data").description("변경할 비밀번호"),
                                        fieldWithPath("checkPassword").description("비밀번호 확인")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("이미 사용중인 닉네임입니다.");
    }

    @DisplayName("userId로 닉네임 변경 실패 _ 해당 id와 일치하는 사용자가 없습니다")
    @Test
    void modifyNicknameByUserIdFail_NotFoundUser() throws Exception {
        //given
        final String api = "/api/users/nickname";
        UserModifyRequest userRequest = UserModifyRequest.builder().checkPassword("checkPassword").data("newPassword").build();
        doThrow(new UserNotFoundException("해당 id와 일치하는 사용자가 없습니다.")).when(userService).updateNickNameByToken(any(), any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/preference/dummyData",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("data").description("변경할 비밀번호"),
                                        fieldWithPath("checkPassword").description("비밀번호 확인")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("해당 id와 일치하는 사용자가 없습니다.");
    }

    @DisplayName("userId로 닉네임 변경")
    @Test
    void modifyNickNAmeByUserId() throws Exception {
        //given
        final String api = "/api/users/nickname";
        UserModifyRequest userRequest = UserModifyRequest.builder().checkPassword("checkPassword").data("newPassword").build();
        doReturn(TodoListMessageResponse.of("닉네임이 변경되었습니다.")).when(userService).updateNickNameByToken(any(), any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("user/preference/modifyNickname",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("data").description("변경할 비밀번호"),
                                        fieldWithPath("checkPassword").description("비밀번호 확인")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("닉네임이 변경되었습니다.");
    }

    @Test
    @DisplayName("사용자 토큰으로 정보 가져오기 _ 사용자 정보를 찾을 수 없음.")
    public void getUserByTokenFail_NotFoundUser() throws Exception {
        //given
        final String api = "/api/users";
        User user = User.builder().password("password").name("name").email("email").build();
        UserResponse userResponse = UserResponse.createUserResponse(user);
        doThrow(new AccountException("사용자 정보를 찾을 수 없습니다.")).when(userService).getUserByToken(any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userResponse))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/preference/dummyData",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("사용자 토큰으로 정보 가져오기")
    public void getUserByToken() throws Exception {
        //given
        final String api = "/api/users";
        User user = User.builder().password("password").name("name").email("email").build();
        UserResponse userResponse = UserResponse.createUserResponse(user);
        doReturn(userResponse).when(userService).getUserByToken(any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(api)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(userResponse))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("user/preference/getUserByToken",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("userId").description("사용자 고유 ID") ,
                                        fieldWithPath("email").description("사용자 email") ,
                                        fieldWithPath("nickname").description("사용자 nickname") ,
                                        fieldWithPath("name").description("사용자 name") ,
                                        fieldWithPath("password").description("사용자 password") ,
                                        fieldWithPath("phone").description("사용자 phone") ,
                                        fieldWithPath("birth").description("출생일"),
                                        fieldWithPath("mbtiType").description("MBTI 타입"),
                                        fieldWithPath("isMarketingAgree").description("마케팅 동의 여부")
                                )
                        )
                );

        final UserResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                UserResponse.class);

        assertThat(result.getName()).isEqualTo("name");
    }

}
