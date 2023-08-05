package com.team.heyyo.todolist.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.todolist.domain.TodoList;
import com.team.heyyo.todolist.dto.TodoListDataResponse;
import com.team.heyyo.todolist.dto.TodoListDateRequest;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import com.team.heyyo.todolist.dto.TodoListRequest;
import com.team.heyyo.todolist.repository.TodoListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @InjectMocks
    TodoListService todoListService;

    @Mock
    TodoListRepository todoListRepository;

    @Mock
    TokenProvider tokenProvider;

    @Test
    @DisplayName("TodoList 저장")
    void saveTodoList() {
        // given
        TodoListRequest todoListRequest = BuildTodoListRequest();
        doReturn(15L).when(tokenProvider).getUserId(any(String.class));
        doReturn(null).when(todoListRepository).save(any(TodoList.class));

        // when
        TodoListMessageResponse result = todoListService.saveTodoList("token" , todoListRequest);

        // then
        assertThat(result.getMessage()).isEqualTo("성공적으로 생성되었습니다.");
    }

    @Test
    @DisplayName("TodoList 삭제")
    void deleteTodoList() {
        // given
        doNothing().when(todoListRepository).deleteTodoListByTodoListId(55L);

        // when
        Throwable throwable = catchThrowable(() -> todoListService.deleteTodoList(55L));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TodoList 수정 실패 _ TodoList 탐색 실패")
    void updateTodoListFail_notFountUser() {
        // given
        TodoListRequest todoListRequest = BuildTodoListRequest();
        doReturn(5L).when(tokenProvider).getUserId(any(String.class));
        doReturn(Optional.empty()).when(todoListRepository).findById(5L);

        // when
        Throwable throwable = catchThrowable(() -> todoListService.updateTodoList("token" , 5L , todoListRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TodoList 수정 실패 _ TodoList 유효하지 않은 접근")
    void updateTodoListFail_validRequest() {
        // given
        TodoListRequest todoListRequest = BuildTodoListRequest();
        doReturn(15L).when(tokenProvider).getUserId(any(String.class));
        doReturn(Optional.empty()).when(todoListRepository).findById(5L);

        // when
        Throwable throwable = catchThrowable(() -> todoListService.updateTodoList("token" , 5L , todoListRequest));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TodoList 수정 성공")
    void updateTodoList() {
        // given
        TodoListRequest todoListRequest = BuildTodoListRequest();
        TodoList todoList = TodoList.of("data" , 5L);
        doReturn(5L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(todoList)).when(todoListRepository).findById(5L);

        // when
        TodoListMessageResponse result = todoListService.updateTodoList("token", 5L , todoListRequest);

        // then
        assertThat(result.getMessage()).isEqualTo("성공적으로 수정되었습니다.");
    }

    @Test
    @DisplayName("TodoList completed 상태 변경 실패 _ 탐색 실패")
    void updateTodoListCompleteFail_notFountTodoList() {
        // given
        doReturn(5L).when(tokenProvider).getUserId(any(String.class));
        doReturn(Optional.empty()).when(todoListRepository).findById(5L);

        // when
        Throwable throwable = catchThrowable(() -> todoListService.updateTodoListComplete("token" , 5L));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TodoList 수정 실패 _ TodoList 유효하지 않은 접근")
    void updateTodoListCompleteFail_validRequest() {
        // given
        doReturn(15L).when(tokenProvider).getUserId(any(String.class));
        doReturn(Optional.empty()).when(todoListRepository).findById(5L);

        // when
        Throwable throwable = catchThrowable(() -> todoListService.updateTodoListComplete("token" , 5L));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("TodoList Completed 상태로 수정 성공")
    void updateTodoListComplete() {
        // given
        TodoList todoList = TodoList.of("data" , 5L);
        doReturn(5L).when(tokenProvider).getUserId("token");
        doReturn(Optional.of(todoList)).when(todoListRepository).findById(5L);

        // when
        TodoListMessageResponse result = todoListService.updateTodoListComplete("token", 5L);

        // then
        assertThat(result.getMessage()).isEqualTo("성공적으로 수정되었습니다.");
    }

    @Test
    @DisplayName("진행중인 TodoList 탐색")
    void findTodoListInProgressByUserId() {
        // given
        List<TodoList> todoListList = new ArrayList<>();
        doReturn(15L).when(tokenProvider).getUserId("token");
        doReturn(todoListList).when(todoListRepository).findTodoListInProgressByUserId(15L);

        // when
        TodoListDataResponse result = todoListService.findTodoListInProgressByUserId("token");

        // then
        assertThat(result.getData()).isNotNull();
    }

    @Test
    @DisplayName("달성한 TodoList 탐색")
    void findAchievedTodoListByUserId() {
        // given
        List<TodoList> todoListList = new ArrayList<>();
        doReturn(15L).when(tokenProvider).getUserId("token");
        doReturn(todoListList).when(todoListRepository).findAchievedTodoListByUserId(15L);

        // when
        TodoListDataResponse result = todoListService.findAchievedTodoListByUserId("token");

        // then
        assertThat(result.getData()).isNotNull();
    }

    @Test
    @DisplayName("달성한 TodoList 탐색")
    void getTodoListByDateAchieved() {
        // given
        List<TodoList> todoListList = new ArrayList<>();
        TodoListDateRequest request = TodoListDateRequest.builder().date("data").build();
        doReturn(15L).when(tokenProvider).getUserId("token");
        doReturn(todoListList).when(todoListRepository).getTodoListByDateAchieved(15L , "data");

        // when
        TodoListDataResponse result = todoListService.getTodoListByDateAchieved("token" , request);

        // then
        assertThat(result.getData()).isNotNull();
    }

    public TodoListRequest BuildTodoListRequest() {
        return TodoListRequest.builder().data("data").build();
    }

}
