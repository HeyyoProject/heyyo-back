package com.team.heyyo.group.chat.controller;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import com.team.heyyo.group.chat.service.ChatService;
import com.team.heyyo.group.chat.service.ParticipantsService;
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

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebAppConfiguration
public class ChatControllerTest {

    @MockBean
    private ChatService chatService;

    @MockBean
    private ParticipantsService participantsService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("채팅 데이터 가져오기")
    @Test
    void findChatDataById() throws Exception {
        //given
        String api = "/chat/{groupStudyId}";

        List<ChatResponse> chatResponses = new LinkedList<>();
        chatResponses.add(ChatResponse.builder().sendTime("2000-01-01 12:00:00").build());

        doReturn(chatResponses).when(chatService).findChatDataByStudyGroupId(40);

        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders
                        .get(api, 40))
                .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("chat/getChatData",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("groupStudyId").description("그룹 스터디 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].chatId").description("채팅 id"),
                                        fieldWithPath("[].sender").description("채팅 전송자 닉네임"),
                                        fieldWithPath("[].message").description("채팅 데이터"),
                                        fieldWithPath("[].sendTime").description("전송 시간")
                                )
                        )
                );
    }

    @DisplayName("현재 접속중인 참가자 가져오기")
    @Test
    void findParticipantsByStudyGroupId() throws Exception {
        //given
        String api = "/chat/participants/{groupStudyId}";

        List<ParticipantsResponse> chatResponses = new LinkedList<>();
        chatResponses.add(ParticipantsResponse.builder().build());


        doReturn(chatResponses).when(participantsService).findParticipantsByStudyGroupId(40);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders
                        .get(api, 40))
                .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("chat/getParticipants",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("groupStudyId").description("그룹 스터디 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[].userId").description("유저 식별자 id"),
                                        fieldWithPath("[].email").description("유저 이메일 데이터"),
                                        fieldWithPath("[].nickname").description("유저 닉네임")
                                )
                        )
                );
    }

}
