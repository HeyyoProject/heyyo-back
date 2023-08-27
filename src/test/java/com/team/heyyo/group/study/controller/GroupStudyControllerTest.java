package com.team.heyyo.group.study.controller;

import com.team.heyyo.group.study.dto.GroupStudyResponse;
import com.team.heyyo.group.study.service.GroupStudyService;
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
    GroupStudyService groupStudyService;

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

        GroupStudyResponse groupStudyResponse1 = GroupStudyResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyResponse groupStudyResponse2 = GroupStudyResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyResponse> list = List.of(groupStudyResponse1, groupStudyResponse2);

        doReturn(list)
                .when(groupStudyService).getRecentGroupStudyList(any());

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

        GroupStudyResponse groupStudyResponse1 = GroupStudyResponse.of("title1", List.of("tag1", "tag2"), 100, true);
        GroupStudyResponse groupStudyResponse2 = GroupStudyResponse.of("title2", List.of("tag1", "tag2", "tag3"), 200, false);
        List<GroupStudyResponse> list = List.of(groupStudyResponse1, groupStudyResponse2);

        doReturn(list)
                .when(groupStudyService).getBestGroupStudyListFromToday(any());

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



}