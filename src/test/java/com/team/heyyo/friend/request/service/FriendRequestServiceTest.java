package com.team.heyyo.friend.request.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.list.repository.FriendRepository;
import com.team.heyyo.friend.request.entity.FriendRequest;
import com.team.heyyo.friend.request.repository.FriendRequestRepository;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
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
public class FriendRequestServiceTest {

    @InjectMocks
    FriendRequestService friendRequestService;

    @Mock
    FriendRequestRepository friendRequestRepository;

    @Mock
    FriendRepository friendRepository;

    @Mock
    TokenProvider tokenProvider;

    @Test
    @DisplayName("친구 요청 목록 저장")
    public void saveFriend() {
        // given
        doReturn(20L).when(tokenProvider).getUserId("token");
        doReturn(null).when(friendRequestRepository).save(any());

        // when
        TodoListMessageResponse result = friendRequestService.saveFriendRequest("token" , 20L);

        // then
        assertThat(result.getMessage()).isEqualTo("요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("친구 요청 목록 제거")
    public void deleteFriendRequest() {
        // given
        doNothing().when(friendRequestRepository).deleteById(any());

        // when
        Throwable throwable = catchThrowable(() -> friendRequestService.deleteFriendRequest(20L));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("친구 요청 목록 불러오기")
    public void getFriendList() {
        // given
        List<UserResponse> list = new ArrayList<>();
        list.add(UserResponse.builder().build());
        doReturn(20L).when(tokenProvider).getUserId("token");
        doReturn(list).when(friendRequestRepository).findFriendRequestByUserId(20L);

        // when
        List<UserResponse> result = friendRequestService.getFriendRequestList("token");

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("친구 요청 승인 실패 _ 찾을 수 없는 엔티티")
    public void approveFriendRequestFail() {
        // given
        doReturn(Optional.empty()).when(friendRequestRepository).findById(20L);

        // when
        Throwable throwable = catchThrowable(() -> friendRequestService.approveTheFriendRequest(20L));

        // then
        assertThat(throwable.getMessage()).isEqualTo("해당 요청을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("친구 요청 승인")
    public void approveFriendRequest() {
        // given
        FriendRequest friendRequest = FriendRequest.of(10L , 20L);
        doReturn(Optional.of(friendRequest)).when(friendRequestRepository).findById(20L);
        doReturn(null).when(friendRepository).save(any());

        // when
        TodoListMessageResponse result = friendRequestService.approveTheFriendRequest(20L);

        // then
        assertThat(result.getMessage()).isEqualTo("요청이 성공적으로 수행되었습니다.");
    }

}
