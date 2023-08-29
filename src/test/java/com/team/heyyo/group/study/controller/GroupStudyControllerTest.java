package com.team.heyyo.group.study.controller;

import com.team.heyyo.group.study.dto.GroupStudyListResponse;
import com.team.heyyo.group.study.service.GroupStudyDetailPageListService;
import com.team.heyyo.group.study.service.GroupStudyMainPageListService;
import com.team.heyyo.user.constant.Mbti;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
class GroupStudyControllerTest {

    MockMvc mockMvc;

    @MockBean
    GroupStudyMainPageListService groupStudyMainPageListService;

    @MockBean
    GroupStudyDetailPageListService groupStudyDetailPageListService;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

    }

    final String API = "/api/group-studies";

    @DisplayName("최근에 생성된 그룹스터디의 리스트들을 반환한다.")
    @Test
    void getRecentGroupStudyList() throws Exception {
        //given
        final String url = API + "/recent";

        GroupStudyListResponse groupStudyListResponse1 = GroupStudyListResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyListResponse groupStudyListResponse2 = GroupStudyListResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyListResponse> list = List.of(groupStudyListResponse1, groupStudyListResponse2);

        doReturn(list)
                .when(groupStudyMainPageListService).getRecentGroupStudyList(any());

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header("Authorization", "Bearer accessToken")
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group-study/getRecentGroupStudyList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("그룹스터디 제목"),
                                fieldWithPath("[].tags[]").description("그룹스터디 태그들"),
                                fieldWithPath("[].viewerCount").description("그룹스터디 시청자수"),
                                fieldWithPath("[].liked").description("현재 사용자가 좋아요를 눌렀는지")
                        )
                ));
    }

    @DisplayName("오늘 하루 좋아요가 많은 그룹스터디의 리스트들을 반환한다.")
    @Test
    void getBestGroupStudyList() throws Exception {
        //given
        final String url = API + "/best";

        GroupStudyListResponse groupStudyListResponse1 = GroupStudyListResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyListResponse groupStudyListResponse2 = GroupStudyListResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyListResponse> list = List.of(groupStudyListResponse1, groupStudyListResponse2);

        doReturn(list)
                .when(groupStudyMainPageListService).getBestGroupStudyListFromToday(any());

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header("Authorization", "Bearer accessToken")
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group-study/getBestGroupStudyList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("그룹스터디 제목"),
                                fieldWithPath("[].tags[]").description("그룹스터디 태그들"),
                                fieldWithPath("[].viewerCount").description("그룹스터디 시청자수"),
                                fieldWithPath("[].liked").description("현재 사용자가 좋아요를 눌렀는지")
                        )
                ));
    }

    @DisplayName("오늘 하루 좋아요가 많은 그룹스터디의 리스트들을 반환한다.")
    @Test
    void getRecommendGroupStudyList() throws Exception {
        //given
        final String url = API + "/recommend";

        GroupStudyListResponse groupStudyListResponse1 = GroupStudyListResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyListResponse groupStudyListResponse2 = GroupStudyListResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyListResponse> list = List.of(groupStudyListResponse1, groupStudyListResponse2);

        doReturn(list)
                .when(groupStudyMainPageListService).getRecommendGroupStudyList(any());

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header("Authorization", "Bearer accessToken")
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group-study/getRecommendGroupStudyList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("그룹스터디 제목"),
                                fieldWithPath("[].tags[]").description("그룹스터디 태그들"),
                                fieldWithPath("[].viewerCount").description("그룹스터디 시청자수"),
                                fieldWithPath("[].liked").description("현재 사용자가 좋아요를 눌렀는지")
                        )
                ));
    }

    @DisplayName("MBTI 에 해당하는 새로생긴 그룹방 리스트를 리턴한다.")
    @Test
    void getRecentMbtiGroupStudyList() throws Exception {
        //given
        final String url = API + "/detail/recent/{mbti}";

        GroupStudyListResponse groupStudyListResponse1 = GroupStudyListResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyListResponse groupStudyListResponse2 = GroupStudyListResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyListResponse> list = List.of(groupStudyListResponse1, groupStudyListResponse2);

        doReturn(list)
                .when(groupStudyDetailPageListService).getRecentGroupStudyDetailList(any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(get(url, Mbti.Focus.name())
                .header("Authorization", "Bearer accessToken")
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group-study/getRecentMbtiGroupStudyList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("mbti").description("해당 키워드 타입")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("그룹스터디 제목"),
                                fieldWithPath("[].tags[]").description("그룹스터디 태그들"),
                                fieldWithPath("[].viewerCount").description("그룹스터디 시청자수"),
                                fieldWithPath("[].liked").description("현재 사용자가 좋아요를 눌렀는지")
                        )
                ));
    }

    @DisplayName("MBTI 에 해당하는 좋아요를 많이 받은 순의 그룹방 리스트를 리턴한다.")
    @Test
    void getMostLikeMbtiGroupStudyList() throws Exception {
        //given
        final String url = API + "/detail/best/{mbti}";

        GroupStudyListResponse groupStudyListResponse1 = GroupStudyListResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyListResponse groupStudyListResponse2 = GroupStudyListResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyListResponse> list = List.of(groupStudyListResponse1, groupStudyListResponse2);

        doReturn(list)
                .when(groupStudyDetailPageListService).getMostLikeGroupStudyDetailList(any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(get(url, Mbti.Focus.name())
                .header("Authorization", "Bearer accessToken")
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group-study/getMostLikeMbtiList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("mbti").description("해당 키워드 타입")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("그룹스터디 제목"),
                                fieldWithPath("[].tags[]").description("그룹스터디 태그들"),
                                fieldWithPath("[].viewerCount").description("그룹스터디 시청자수"),
                                fieldWithPath("[].liked").description("현재 사용자가 좋아요를 눌렀는지")
                        )
                ));
    }

    @DisplayName("사용자의 MBTI와 반대되는 그룹방의 리스트를 리턴한다.")
    @Test
    void getOppositeMbtiGroupStudyList() throws Exception {
        //given
        final String url = API + "/detail/opposite";

        GroupStudyListResponse groupStudyListResponse1 = GroupStudyListResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyListResponse groupStudyListResponse2 = GroupStudyListResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyListResponse> list = List.of(groupStudyListResponse1, groupStudyListResponse2);

        doReturn(list)
                .when(groupStudyDetailPageListService).getOppositeUserMbtiGroupStudyList(any());

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header("Authorization", "Bearer accessToken")
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("group-study/getOppositeMbtiGroupStudyList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].title").description("그룹스터디 제목"),
                                fieldWithPath("[].tags[]").description("그룹스터디 태그들"),
                                fieldWithPath("[].viewerCount").description("그룹스터디 시청자수"),
                                fieldWithPath("[].liked").description("현재 사용자가 좋아요를 눌렀는지")
                        )
                ));
    }



}