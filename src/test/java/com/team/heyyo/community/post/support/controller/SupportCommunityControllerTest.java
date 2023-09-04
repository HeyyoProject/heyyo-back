package com.team.heyyo.community.post.support.controller;

import com.google.gson.Gson;
import com.team.heyyo.community.post.support.community.domain.SupportCommunity;
import com.team.heyyo.community.post.support.community.domain.SupportCommunityType;
import com.team.heyyo.community.post.support.community.dto.CommentRequest;
import com.team.heyyo.community.post.support.community.dto.NewSupportCommunityRequest;
import com.team.heyyo.community.post.support.community.dto.SupportCommunityRequest;
import com.team.heyyo.community.post.support.community.exception.SupportCommunityException;
import com.team.heyyo.community.post.support.community.service.SupportCommunityService;
import com.team.heyyo.community.post.support.tag.domain.SupportCommunityTagData;
import com.team.heyyo.util.GsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebAppConfiguration
public class SupportCommunityControllerTest {

    @MockBean
    private SupportCommunityService supportCommunityService;

    Gson gson;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        gson = GsonUtil.getGsonInstance();
    }

    @Test
    @DisplayName("고객센터 게시물 저장")
    public void saveSupportCommunity() throws Exception {
        // given
        String api = "/api/support";
        NewSupportCommunityRequest request = NewSupportCommunityRequest.builder().supportCommunityType(SupportCommunityType.ANNOUNCEMENT).tag(new String[]{"tag1", "tag2"}).title("title").content("content").build();

        doNothing().when(supportCommunityService).saveSupportCommunity(any(), any());

        // when
        ResultActions resultActions = mockMvc.perform(post(api)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(request))
                .header("Authorization", "Bearer accessToken"));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(document("support/saveSupport",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                requestFields(
                                        fieldWithPath("supportCommunityType").description("게시글 타입 (FREQUENTLY_ASKED_QUESTIONS , ANNOUNCEMENT)"),
                                        fieldWithPath("title").description("게시글 제목"),
                                        fieldWithPath("content").description("게시글 컨텐츠"),
                                        fieldWithPath("tag.[]").description("태그 데이터")
                                )
                        )
                );
    }

    @Test
    @DisplayName("고객센터 커멘트 등록 및 해결")
    public void updateCommentAndIsSolved() throws Exception {
        // given
        String api = "/api/support/{postId}";
        CommentRequest commentRequest = CommentRequest.builder().message("message").build();

        doNothing().when(supportCommunityService).updateCommentAndIsSolved(30L, commentRequest);

        // when
        ResultActions resultActions = mockMvc.perform(patch(api, "30")
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(commentRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("support/updateCommentAndIsSolved",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("포스팅 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("message").description("커멘트 데이터")
                                )
                        )
                );
    }

    @Test
    @DisplayName("고객센터 커멘트 등록 및 해결 실패 _ 찾을 수 없는 게시판")
    public void updateCommentAndIsSolvedFail_notFount() throws Exception {
        // given
        String api = "/api/support/{postId}";
        CommentRequest commentRequest = CommentRequest.builder().message("message").build();

        doThrow(new SupportCommunityException("해당 게시물을 찾을 수 없습니다.")).when(supportCommunityService).updateCommentAndIsSolved(anyLong(), any());

        // when
        ResultActions resultActions = mockMvc.perform(patch(api, "30")
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(commentRequest)));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("support/dummy",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("포스팅 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("message").description("커멘트 데이터")
                                )
                        )
                );
    }

    @Test
    @DisplayName("타입으로 게시판 데이터 찾기")
    public void findSupportCommunityResponseBySupportCommunityType() throws Exception {
        // given
        String api = "/api/support";
        SupportCommunityRequest request = SupportCommunityRequest.builder().supportCommunityType(SupportCommunityType.ANNOUNCEMENT).build();
        doReturn(buildSupportCommunity()).when(supportCommunityService).findSupportCommunityResponseBySupportCommunityType(any(), any());

        // when
        ResultActions resultActions = mockMvc.perform(get(api)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(request)));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("support/searchPostByType",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("supportCommunityType").description("게시글 타입 (FREQUENTLY_ASKED_QUESTIONS , ANNOUNCEMENT)")
                                ),
                                responseFields(
                                        fieldWithPath("[].supportCommunityId").description("고유 ID"),
                                        fieldWithPath("[].title").description("타이틀 제목"),
                                        fieldWithPath("[].content").description("내용"),
                                        fieldWithPath("[].userId").description("작성자 ID"),
                                        fieldWithPath("[].adminComment").description("운영자 작성 여부"),
                                        fieldWithPath("[].isSolved").description("해결 여부"),
                                        fieldWithPath("[].supportCommunityType").description("게시글 타입"),
                                        fieldWithPath("[].tagData").description("태그 데이터"),
                                        fieldWithPath("[].tagData[].supportTagKey").description("태그 데이터 키 값"),
                                        fieldWithPath("[].tagData[].tag").description("태그 데이터")

                                )
                        )
                );
    }

    @Test
    @DisplayName("타입으로 게시판 데이터 갯수 찾기")
    public void findSupportCommunityResponseCountBySupportCommunityType() throws Exception {
        // given
        String api = "/api/support/count";
        SupportCommunityRequest request = SupportCommunityRequest.builder().supportCommunityType(SupportCommunityType.ANNOUNCEMENT).build();
        doReturn(5L).when(supportCommunityService).findSupportCommunityResponseCountBySupportCommunityType(any());

        // when
        ResultActions resultActions = mockMvc.perform(get(api)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(request)));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("support/searchPostCountByType",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("supportCommunityType").description("게시글 타입 (FREQUENTLY_ASKED_QUESTIONS , ANNOUNCEMENT)")
                                )
                        )
                );
    }

    @Test
    @DisplayName("타입과 검색어로 게시판 데이터 찾기")
    public void findSupportCommunityResponseBySupportCommunityTypeAndSearch() throws Exception {
        // given
        String api = "/api/support/search?search=data";
        SupportCommunityRequest request = SupportCommunityRequest.builder().supportCommunityType(SupportCommunityType.ANNOUNCEMENT).build();
        doReturn(buildSupportCommunity()).when(supportCommunityService).findSupportCommunityResponseBySupportCommunityTypeAndSearch(any(), any(), any());

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(api)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(request)));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("support/searchPostByTypeAndSearch",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("search").description("검색어")
                                ),
                                requestFields(
                                        fieldWithPath("supportCommunityType").description("게시글 타입 (FREQUENTLY_ASKED_QUESTIONS , ANNOUNCEMENT)")
                                ),
                                responseFields(
                                        fieldWithPath("[].supportCommunityId").description("고유 ID"),
                                        fieldWithPath("[].title").description("타이틀 제목"),
                                        fieldWithPath("[].content").description("내용"),
                                        fieldWithPath("[].userId").description("작성자 ID"),
                                        fieldWithPath("[].adminComment").description("운영자 작성 여부"),
                                        fieldWithPath("[].isSolved").description("해결 여부"),
                                        fieldWithPath("[].supportCommunityType").description("게시글 타입"),
                                        fieldWithPath("[].tagData").description("태그 데이터"),
                                        fieldWithPath("[].tagData[].supportTagKey").description("태그 데이터 키 값"),
                                        fieldWithPath("[].tagData[].tag").description("태그 데이터")

                                )
                        )
                );
    }

    @Test
    @DisplayName("타입과 검색어로 게시판 데이터 갯수 찾기")
    public void findSupportCommunityResponseCountBySupportCommunityTypeAndSearch() throws Exception {
        // given
        String api = "/api/support/search/count?search=data";
        SupportCommunityRequest request = SupportCommunityRequest.builder().supportCommunityType(SupportCommunityType.ANNOUNCEMENT).build();
        doReturn(5L).when(supportCommunityService).findSupportCommunityResponseCountBySupportCommunityTypeAndSearch(any(), any());

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(api)
                .contentType(APPLICATION_JSON)
                .content(gson.toJson(request)));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("support/searchPostCountByTypeAndSearch",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("search").description("검색어")
                                ),
                                requestFields(
                                        fieldWithPath("supportCommunityType").description("게시글 타입 (FREQUENTLY_ASKED_QUESTIONS , ANNOUNCEMENT)")
                                )
                        )
                );
    }

    @Test
    @DisplayName("ID로 고객센터 게시물 탐색")
    public void findSupportCommunityResponseById() throws Exception {
        // given
        String api = "/api/support/{postId}";

        List<SupportCommunityTagData> tata = new ArrayList<>();
        tata.add(SupportCommunityTagData.buildTagData("message"));
        SupportCommunity result = SupportCommunity.builder().title("title").tagData(tata).supportCommunityType(SupportCommunityType.ANNOUNCEMENT).content("content").build();
        doReturn(result).when(supportCommunityService).findSupportCommunityResponseById(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(api, "30"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("support/findPostById",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("게시물 이름")
                                ),
                                responseFields(
                                        fieldWithPath("supportCommunityId").description("고유 ID"),
                                        fieldWithPath("title").description("타이틀 제목"),
                                        fieldWithPath("content").description("내용"),
                                        fieldWithPath("userId").description("작성자 ID"),
                                        fieldWithPath("adminComment").description("운영자 작성 여부"),
                                        fieldWithPath("isSolved").description("해결 여부"),
                                        fieldWithPath("supportCommunityType").description("게시글 타입"),
                                        fieldWithPath("tagData").description("태그 데이터"),
                                        fieldWithPath("tagData[].supportTagKey").description("태그 데이터 키 값"),
                                        fieldWithPath("tagData[].tag").description("태그 데이터")

                                )
                        )
                );
    }

    @Test
    @DisplayName("ID로 고객센터 게시물 탐색 실패 _ 사용자 정보를 찾을 수 없음")
    public void findSupportCommunityResponseByIdFail_NotFount() throws Exception {
        // given
        String api = "/api/support/{postId}";

        doThrow(new SupportCommunityException("해당 게시물을 찾을 수 없습니다.")).when(supportCommunityService).findSupportCommunityResponseById(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(api, "30"));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("support/dummy",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("포스팅 아이디")
                                )
                        )
                );
    }


    public List<SupportCommunity> buildSupportCommunity() {
        List<SupportCommunity> result = new ArrayList<>();

        List<SupportCommunityTagData> tata = new ArrayList<>();
        tata.add(SupportCommunityTagData.buildTagData("message"));
        tata.add(SupportCommunityTagData.buildTagData("message"));

        result.add(SupportCommunity.builder().title("title").supportCommunityType(SupportCommunityType.ANNOUNCEMENT).content("content").tagData(tata).build());

        return result;
    }
}
