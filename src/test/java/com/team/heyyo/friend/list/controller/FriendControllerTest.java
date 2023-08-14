package com.team.heyyo.friend.list.controller;

import com.google.gson.Gson;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.list.dto.FriendRequestDto;
import com.team.heyyo.friend.list.service.FriendService;
import com.team.heyyo.todolist.controller.TodoListController;
import com.team.heyyo.todolist.dto.TodoListDataRequest;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.todolist.service.TodoListService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(FriendController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
public class FriendControllerTest {
    MockMvc mockMvc;
    Gson gson;

    @MockBean
    FriendService friendService;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        gson = GsonUtil.getGsonInstance();
    }

    @Test
    @DisplayName("Friend 저장")
    public void saveFriend() throws Exception {
        // given
        final String url = "/api/friend";
        FriendRequestDto friendRequestDto = FriendRequestDto.builder().toUserId(10L).build();
        doReturn(TodoListMessageResponse.of("요청이 성공적으로 수행되었습니다.")).when(friendService).saveFriend(any() , anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(url)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(friendRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated()).andDo(
                document("friend/saveFriend",
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
    @DisplayName("Friend 삭제")
    public void deleteFriend() throws Exception {
        // given
        final String url = "/api/friend";
        FriendRequestDto friendRequestDto = FriendRequestDto.builder().toUserId(10L).build();
        doNothing().when(friendService).deleteFriend(any(Long.class));

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(url)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(friendRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNoContent()).andDo(
                document("friend/deleteFriend",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        requestFields(
                                fieldWithPath("toUserId").description("대상 유저의 ID")
                        )
                )
        );
    }

    @Test
    @DisplayName("Friend 리스트 불러오기")
    public void getFriendList() throws Exception {
        // given
        final String url = "/api/friend";
        List<UserResponse> list = new ArrayList<>();
        list.add(buildUserResponse());
        doReturn(list).when(friendService).getFriendList(any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(url)
                        .header("Authorization", "Bearer accessToken")
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("friend/getFriend",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").description("대상 유저의 ID"),
                                fieldWithPath("[].birth").description("생일"),
                                fieldWithPath("[]phone").description("핸드폰 번호"),
                                fieldWithPath("[].characterType").description("캐릭터 타입"),
                                fieldWithPath("[].email").description("email"),
                                fieldWithPath("[].mbtiType").description("MBTI 타입"),
                                fieldWithPath("[].name").description("이름"),
                                fieldWithPath("[].nickname").description("닉네임"),
                                fieldWithPath("[].password").description("비밀 번호")
                        )
                )
        );
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
