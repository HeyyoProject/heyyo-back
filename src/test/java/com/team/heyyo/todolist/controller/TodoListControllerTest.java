package com.team.heyyo.todolist.controller;


import com.google.gson.Gson;
import com.team.heyyo.todolist.domain.TodoList;
import com.team.heyyo.todolist.dto.TodoListDataResponse;
import com.team.heyyo.todolist.dto.TodoListDateRequest;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.todolist.dto.TodoListDataRequest;
import com.team.heyyo.todolist.exception.TodoListException;
import com.team.heyyo.todolist.service.TodoListService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
@WebMvcTest(TodoListController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebAppConfiguration
public class TodoListControllerTest {

    MockMvc mockMvc;
    Gson gson;

    @MockBean
    TodoListService todoListService;

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        gson = GsonUtil.getGsonInstance();
    }

    @Test
    @DisplayName("TodoList 저장 완료")
    public void saveTodoList() throws Exception {
        // given
        final String url = "/api/todo";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doReturn(TodoListMessageResponse.of("성공적으로 생성되었습니다.")).when(todoListService).saveTodoList(any() , any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post(url)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated()).andDo(
                document("todoList/saveTodoList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("성공적으로 생성되었습니다.");
    }

    @Test
    @DisplayName("TodoList 삭제 완료")
    public void deleteTodoList() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}";
        doNothing().when(todoListService).deleteTodoList(30L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(url , "30")
        );

        // then
        resultActions.andExpect(status().isNoContent()).andDo(
                document("todoList/deleteTodoList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        )
                )
        );
    }

    @Test
    @DisplayName("TodoList 수정 실패_데이터를 찾을 수 없음")
    public void updateTodoListFail_notFountData() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doThrow(new TodoListException("해당 데이터를 찾을 수 없습니다.")).when(todoListService).updateTodoList(any() , eq(30L) , any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(url , "30")
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest()).andDo(
                document("todoList/dummy",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("해당 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TodoList 수정 실패_유효하지 않은 요청")
    public void updateTodoListFail_invalidRequest() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doThrow(new TodoListException("비정상적인 접근입니다.")).when(todoListService).updateTodoList(any() , eq(30L) , any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(url , "30")
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest()).andDo(
                document("todoList/dummy",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("비정상적인 접근입니다.");
    }

    @Test
    @DisplayName("TodoList 수정 완료")
    public void updateTodoList() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doReturn(TodoListMessageResponse.of("성공적으로 수정되었습니다.")).when(todoListService).updateTodoList(any() , eq(30L) , any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(url , "30")
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("todoList/updateTodoList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("성공적으로 수정되었습니다.");
    }

    @Test
    @DisplayName("TodoList 완료 실패 _ 데이터를 찾을 수 없음.")
    public void updateTodoListCompleteFail_notFountData() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}/complete";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doThrow(new TodoListException("해당 데이터를 찾을 수 없습니다.")).when(todoListService).updateTodoListComplete(any() , eq(30L));

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(url , "30")
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest()).andDo(
                document("todoList/dummy",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("해당 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TodoList 완료 실패 _ 유효하지 않은 요청.")
    public void updateTodoListCompleteFail_invalidRequest() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}/complete";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doThrow(new TodoListException("비정상적인 접근입니다.")).when(todoListService).updateTodoListComplete(any() , eq(30L));

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(url , "30")
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest()).andDo(
                document("todoList/dummy",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("비정상적인 접근입니다.");
    }

    @Test
    @DisplayName("TodoList 완료")
    public void updateTodoListComplete() throws Exception {
        // given
        final String url = "/api/todo/{todoListId}/complete";
        TodoListDataRequest todoListDataRequest = TodoListDataRequest.builder().data("data").build();
        doReturn(TodoListMessageResponse.of("성공적으로 수정되었습니다.")).when(todoListService).updateTodoListComplete(any() , eq(30L));

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch(url , "30")
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListDataRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("todoList/updateTodoListComplete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoListId").description("TodoList Id")
                        ),
                        requestFields(
                                fieldWithPath("data").description("요청 데이터")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("message").description("결과 메세지")
                        )
                )
        );

        final TodoListMessageResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListMessageResponse.class);

        assertThat(result.getMessage()).isEqualTo("성공적으로 수정되었습니다.");
    }

    @Test
    @DisplayName("현재 진행되고 있는 TodoList 탐색")
    public void findTodoListInProgressByUserId() throws Exception {
        // given
        final String url = "/api/todo/progress";
        TodoListDataResponse todoListDataResponse = TodoListDataResponse.of(createTodoLists());
        doReturn(todoListDataResponse).when(todoListService).findTodoListInProgressByUserId(any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(url)
                        .header("Authorization", "Bearer accessToken")
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("todoList/findTodoListInProgressByUserId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("data").description("결과 데이터"),
                                fieldWithPath("data[].todoListId").description("todoList 식별자"),
                                fieldWithPath("data[].data").description("todoList 데이터"),
                                fieldWithPath("data[].userId").description("todoList 유저 정보"),
                                fieldWithPath("data[].completedDate").description("todoList 완료 날짜"),
                                fieldWithPath("data[].isComplete").description("todoList 완료 여부")
                        )
                )
        );

        final TodoListDataResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListDataResponse.class);

        assertThat(((List) result.getData()).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("달성한 TodoList 탐색")
    public void findAchievedTodoListByUserId() throws Exception {
        // given
        final String url = "/api/todo/achieve";
        TodoListDataResponse todoListDataResponse = TodoListDataResponse.of(createTodoLists());
        doReturn(todoListDataResponse).when(todoListService).findAchievedTodoListByUserId(any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(url)
                        .header("Authorization", "Bearer accessToken")
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("todoList/findAchievedTodoListByUserId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        responseFields(
                                fieldWithPath("data").description("결과 데이터"),
                                fieldWithPath("data[].todoListId").description("todoList 식별자"),
                                fieldWithPath("data[].data").description("todoList 데이터"),
                                fieldWithPath("data[].userId").description("todoList 유저 정보"),
                                fieldWithPath("data[].completedDate").description("todoList 완료 날짜"),
                                fieldWithPath("data[].isComplete").description("todoList 완료 여부")
                        )
                )
        );

        final TodoListDataResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListDataResponse.class);

        assertThat(((List) result.getData()).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 날짜에 완료한 TodoList 탐색")
    public void getTodoListByDateAchieved() throws Exception {
        // given
        final String url = "/api/todo/achieve/date";
        TodoListDataResponse todoListDataResponse = TodoListDataResponse.of(createTodoLists());
        TodoListDateRequest todoListRequest = TodoListDateRequest.builder().date("2020-07-02").build();
        doReturn(todoListDataResponse).when(todoListService).getTodoListByDateAchieved(any() , any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(url)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("todoList/getTodoListByDateAchieved",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        requestFields(
                                fieldWithPath("date").description("날짜 데이터")
                        ),
                        responseFields(
                                fieldWithPath("data").description("결과 데이터"),
                                fieldWithPath("data[].todoListId").description("todoList 식별자"),
                                fieldWithPath("data[].data").description("todoList 데이터"),
                                fieldWithPath("data[].userId").description("todoList 유저 정보"),
                                fieldWithPath("data[].completedDate").description("todoList 완료 날짜"),
                                fieldWithPath("data[].isComplete").description("todoList 완료 여부")
                        )
                )
        );

        final TodoListDataResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListDataResponse.class);

        assertThat(((List) result.getData()).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 월에 달선한 TodoList 탐색")
    public void getTodoListForASpecificMonth() throws Exception {
        // given
        final String url = "/api/todo/achieve/month";
        TodoListDataResponse todoListDataResponse = TodoListDataResponse.of(createTodoLists());
        TodoListDateRequest todoListRequest = TodoListDateRequest.builder().date("2020-07-02").build();
        doReturn(todoListDataResponse).when(todoListService).getTodoListForASpecificMonth(any() , any());

        // when
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get(url)
                        .header("Authorization", "Bearer accessToken")
                        .content(gson.toJson(todoListRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(
                document("todoList/getTodoListForASpecificMonth",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 access token")
                        ),
                        requestFields(
                                fieldWithPath("date").description("날짜 데이터")
                        ),
                        responseFields(
                                fieldWithPath("data").description("결과 데이터"),
                                fieldWithPath("data[].todoListId").description("todoList 식별자"),
                                fieldWithPath("data[].data").description("todoList 데이터"),
                                fieldWithPath("data[].userId").description("todoList 유저 정보"),
                                fieldWithPath("data[].completedDate").description("todoList 완료 날짜"),
                                fieldWithPath("data[].isComplete").description("todoList 완료 여부")
                        )
                )
        );

        final TodoListDataResponse result = gson.fromJson(
                resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
                TodoListDataResponse.class);

        assertThat(((List) result.getData()).size()).isEqualTo(3);
    }

    public List<TodoList> createTodoLists() {
        List<TodoList> todoListList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            todoListList.add(TodoList.of("data" , 30L));
        }

        return todoListList;
    }
}
