package com.team.heyyo.group.reference.controller;

import com.team.heyyo.group.reference.constant.ReferenceOrderBy;
import com.team.heyyo.group.reference.dto.ReferenceListResponse;
import com.team.heyyo.group.reference.dto.ReferenceSwiperListResponse;
import com.team.heyyo.group.reference.service.ReferenceService;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebAppConfiguration
class ReferenceControllerTest {

    @MockBean
    private ReferenceService referenceService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    final String API = "/api/group-study/reference";

    @DisplayName("오늘 하루 좋아요를 많이 받은 자료를 조회한다")
    @Test
    void today_most_like_reference() throws Exception {
        //given
        final String accessToken = "accessToken";

        ReferenceSwiperListResponse referenceList1 = ReferenceSwiperListResponse.of(1L, "title1", true, "imageUrl");
        ReferenceSwiperListResponse referenceList2 = ReferenceSwiperListResponse.of(2L, "title2", false, "imageUrl");

        when(referenceService.findTodayMostLikeReference(any()))
                .thenReturn(List.of(referenceList1, referenceList2));
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/best")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group/reference/best/success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].referenceId").description("자료 고유 id"),
                                fieldWithPath("[].title").description("자료 제목"),
                                fieldWithPath("[].isScraped").description("자료 좋아요 여부"),
                                fieldWithPath("[].imageUrl").description("자료 이미지 url")
                            )
                        )
                );
    }

    @DisplayName("자료실 리스트를 조회한다 : 기본으로 인기순 12개를 리턴한다.")
    @Test
    void getReferenceList() throws Exception {
        //given
        ReferenceListResponse response1 = ReferenceListResponse.of(
                1L,
                "title1",
                "description1",
                List.of("tag1", "tag2"),
                true,
                "imageUrl"
        );
        ReferenceListResponse response2 = ReferenceListResponse.of(
                2L,
                "title2",
                "description2",
                List.of("tag1", "tag2"),
                false,
                "imageUrl"
        );


        when(referenceService.findReferenceList(any(), any(), any()))
                .thenReturn(List.of(response1, response2));

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/list")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer accessToken")
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group/reference/list/success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].referenceId").description("자료 고유 id"),
                                fieldWithPath("[].title").description("자료 제목"),
                                fieldWithPath("[].description").description("자료 설명"),
                                fieldWithPath("[].tags").description("자료 카테고리"),
                                fieldWithPath("[].isScraped").description("자료 좋아요 여부"),
                                fieldWithPath("[].imageUrl").description("자료 이미지 url")
                        )
                ));
    }

    @DisplayName("자료실 리스트 6개를 조회한다")
    @Test
    void getReferenceListLimit6() throws Exception {
        //given
        ReferenceListResponse response1 = ReferenceListResponse.of(
                1L,
                "title1",
                "description1",
                List.of("tag1", "tag2"),
                true,
                "imageUrl"
        );
        ReferenceListResponse response2 = ReferenceListResponse.of(
                2L,
                "title2",
                "description2",
                List.of("tag1", "tag2"),
                false,
                "imageUrl"
        );


        when(referenceService.findReferenceList(any(), any(), any()))
                .thenReturn(List.of(response1, response2));

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/list")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer accessToken")
                        .param("size", "6")
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group/reference/list/success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("accessToken")
                        ),
                        queryParameters(
                                parameterWithName("size").description("조회할 자료 개수")
                        ),
                        responseFields(
                                fieldWithPath("[].referenceId").description("자료 고유 id"),
                                fieldWithPath("[].title").description("자료 제목"),
                                fieldWithPath("[].description").description("자료 설명"),
                                fieldWithPath("[].tags").description("자료 카테고리"),
                                fieldWithPath("[].isScraped").description("자료 좋아요 여부"),
                                fieldWithPath("[].imageUrl").description("자료 이미지 url")
                        )
                ));
    }

    @DisplayName("자료실 리스트 6개를 조회순으로 조회한다")
    @Test
    void getViewsReferenceListLimit6() throws Exception {
        //given
        ReferenceListResponse response1 = ReferenceListResponse.of(
                1L,
                "title1",
                "description1",
                List.of("tag1", "tag2"),
                true,
                "imageUrl"
        );
        ReferenceListResponse response2 = ReferenceListResponse.of(
                2L,
                "title2",
                "description2",
                List.of("tag1", "tag2"),
                false,
                "imageUrl"
        );


        when(referenceService.findReferenceList(any(), any(), any()))
                .thenReturn(List.of(response1, response2));

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/list")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer accessToken")
                        .param("orderBy", ReferenceOrderBy.VIEWS.name())
                        .param("size", "6")
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group/reference/list/views/success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("accessToken")
                        ),
                        queryParameters(
                                parameterWithName("orderBy").description("조회할 자료 정렬 기준"),
                                parameterWithName("size").description("조회할 자료 개수")
                        ),
                        responseFields(
                                fieldWithPath("[].referenceId").description("자료 고유 id"),
                                fieldWithPath("[].title").description("자료 제목"),
                                fieldWithPath("[].description").description("자료 설명"),
                                fieldWithPath("[].tags").description("자료 카테고리"),
                                fieldWithPath("[].isScraped").description("자료 좋아요 여부"),
                                fieldWithPath("[].imageUrl").description("자료 이미지 url")
                        )
                ));
    }

    @DisplayName("자료실 리스트 12개를 최신순으로 조회한다")
    @Test
    void getRecentReferenceListLimit6() throws Exception {
        //given
        ReferenceListResponse response1 = ReferenceListResponse.of(
                1L,
                "title1",
                "description1",
                List.of("tag1", "tag2"),
                true,
                "imageUrl"
        );
        ReferenceListResponse response2 = ReferenceListResponse.of(
                2L,
                "title2",
                "description2",
                List.of("tag1", "tag2"),
                false,
                "imageUrl"
        );


        when(referenceService.findReferenceList(any(), any(), any()))
                .thenReturn(List.of(response1, response2));

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/list")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer accessToken")
                        .param("orderBy", ReferenceOrderBy.RECENT.name())
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group/reference/list/recent/success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("accessToken")
                        ),
                        queryParameters(
                                parameterWithName("orderBy").description("조회할 자료 정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("[].referenceId").description("자료 고유 id"),
                                fieldWithPath("[].title").description("자료 제목"),
                                fieldWithPath("[].description").description("자료 설명"),
                                fieldWithPath("[].tags").description("자료 카테고리"),
                                fieldWithPath("[].isScraped").description("자료 좋아요 여부"),
                                fieldWithPath("[].imageUrl").description("자료 이미지 url")
                        )
                ));
    }

    @DisplayName("자료실 리스트 12개를 인기순으로 2페이지를 조회한다")
    @Test
    void getBestReferencePage2() throws Exception {
        //given
        ReferenceListResponse response1 = ReferenceListResponse.of(
                1L,
                "title1",
                "description1",
                List.of("tag1", "tag2"),
                true,
                "imageUrl"
        );
        ReferenceListResponse response2 = ReferenceListResponse.of(
                2L,
                "title2",
                "description2",
                List.of("tag1", "tag2"),
                false,
                "imageUrl"
        );


        when(referenceService.findReferenceList(any(), any(), any()))
                .thenReturn(List.of(response1, response2));

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/list")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer accessToken")
                        .param("page", "1")
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group/reference/list/recent/success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("accessToken")
                        ),
                        queryParameters(
                                parameterWithName("page").description("조회할 자료 페이지 (2페이지 이면 1 , 1페이지 이면 0")
                        ),
                        responseFields(
                                fieldWithPath("[].referenceId").description("자료 고유 id"),
                                fieldWithPath("[].title").description("자료 제목"),
                                fieldWithPath("[].description").description("자료 설명"),
                                fieldWithPath("[].tags").description("자료 카테고리"),
                                fieldWithPath("[].isScraped").description("자료 좋아요 여부"),
                                fieldWithPath("[].imageUrl").description("자료 이미지 url")
                        )
                ));
    }


}