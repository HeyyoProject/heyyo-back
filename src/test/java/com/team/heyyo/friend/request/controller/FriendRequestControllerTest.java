package com.team.heyyo.friend.request.controller;

import com.google.gson.Gson;
import com.team.heyyo.advice.ErrorResponse;
import com.team.heyyo.friend.dto.FriendRequestDto;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.exception.FriendException;
import com.team.heyyo.friend.request.service.FriendRequestService;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.constant.UserCharacterType;
import com.team.heyyo.util.GsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(FriendRequestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
public class FriendRequestControllerTest {

    MockMvc mockMvc;
    Gson gson;

    @MockBean
    FriendRequestService friendRequestService;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        gson = GsonUtil.getGsonInstance();
    }

    @Test
    @DisplayName("친구 요청 하기")
    public void saveFriend() throws Exception {
        // given
        final String url = "/api/friend-request";
        FriendRequestDto friendRequestDto = FriendRequestDto.builder().toUserId(10L).build();
        doReturn(TodoListMessageResponse.of("요청이 성공적으로 수행되었습니다.")).when(friendRequestService).saveFriendRequest(any() , anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(url)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(friendRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated()).andDo(
                document("friend-request/saveFriendRequest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        requestFields(
                                fieldWithPath("toUserId").description("대상 유저의 ID")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("친구 요청 삭제")
    public void deleteFriendRequest() throws Exception {
        // given
        final String url = "/api/friend-request/{requestId}";
        FriendRequestDto friendRequestDto = FriendRequestDto.builder().toUserId(10L).build();
        doNothing().when(friendRequestService).deleteFriendRequest(30L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(url , 30L)
                        .content(gson.toJson(friendRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNoContent()).andDo(
                document("friend-request/deleteFriendRequest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("requestId").description("Request ID 값")
                        ),
                        requestFields(
                                fieldWithPath("toUserId").description("대상 유저의 ID")
                        )
                )
        );
    }

    @Test
    @DisplayName("Friend 요청 리스트 불러오기")
    public void getFriendRequestList() throws Exception {
        // given
        final String url = "/api/friend-request";
        List<UserResponse> list = new ArrayList<>();
        list.add(buildUserResponse());
        doReturn(list).when(friendRequestService).getFriendRequestList(any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(url)
                        .header("Authorization", "Bearer accessToken")
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("friend-request/getFriendRequest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").description("대상 유저의 ID"),
                                fieldWithPath("[].birth").description("생일"),
                                fieldWithPath("[].phone").description("핸드폰 번호"),
                                fieldWithPath("[].characterType").description("캐릭터 타입"),
                                fieldWithPath("[].email").description("email"),
                                fieldWithPath("[].mbtiType").description("MBTI 타입"),
                                fieldWithPath("[].name").description("이름"),
                                fieldWithPath("[].nickname").description("닉네임"),
                                fieldWithPath("[].password").description("비밀 번호")
                        )
                )
        );

        final List<UserResponse> result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                List.class);

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("친구 요청 수락 실패 _ 찾을 수 없는 요청")
    public void approveTheFriendRequestFail() throws Exception {
        // given
        final String url = "/api/friend-request/approve/{requestId}";
        doThrow(new FriendException("해당 요청을 찾을 수 없습니다.")).when(friendRequestService).approveTheFriendRequest(anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(url , 30L)
        );

        // then
        resultActions.andExpect(status().isBadRequest());

        final ErrorResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                ErrorResponse.class);

        assertThat(result.getMessage()).isEqualTo("해당 요청을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("친구 요청 수락")
    public void approveTheFriendRequest() throws Exception {
        // given
        final String url = "/api/friend-request/approve/{requestId}";
        doReturn(TodoListMessageResponse.of("요청이 성공적으로 수행되었습니다.")).when(friendRequestService).approveTheFriendRequest(anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(url , 30L)
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("friend-request/approveFriendRequest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("requestId").description("Request ID 값")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("요청이 성공적으로 수행되었습니다.");

    }

    public UserResponse buildUserResponse() {
        return UserResponse.builder()
                .userId(10L)
                .birth(new Date())
                .phone("0100000000")
                .characterType(UserCharacterType.고독)
                .email("email")
                .mbtiType(Mbti.Focus)
                .name("name")
                .nickname("nickName")
                .password("password")
                .build();
    }

}
