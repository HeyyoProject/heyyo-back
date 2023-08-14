package com.team.heyyo.friend.list.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.list.repository.FriendRepository;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @InjectMocks
    FriendService friendService;

    @Mock
    FriendRepository friendRepository;

    @Mock
    TokenProvider tokenProvider;

    @Test
    @DisplayName("친구 저장")
    public void saveFriend() {
        // given
        doReturn(20L).when(tokenProvider).getUserId("token");
        doReturn(null).when(friendRepository).save(any());

        // when
        TodoListMessageResponse result = friendService.saveFriend("token" , 20L);

        // then
        assertThat(result.getMessage()).isEqualTo("요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("친구 삭제")
    public void deleteFriend() {
        // given
        doNothing().when(friendRepository).deleteById(20L);

        // when
        Throwable throwable = catchThrowable(() -> friendService.deleteFriend(20L));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("친구 목록 불러오기")
    public void getFriendList() {
        // given
        List<UserResponse> list = new ArrayList<>();
        list.add(UserResponse.builder().build());
        doReturn(20L).when(tokenProvider).getUserId("token");
        doReturn(list).when(friendRepository).findFriendByUserId(20L);

        // when
        List<UserResponse> result = friendService.getFriendList("token");

        // then
        assertThat(result.size()).isEqualTo(1);
    }

}
