package com.team.heyyo.friend.request.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.exception.FriendException;
import com.team.heyyo.friend.list.domain.Friend;
import com.team.heyyo.friend.list.repository.FriendRepository;
import com.team.heyyo.friend.request.entity.FriendRequest;
import com.team.heyyo.friend.request.repository.FriendRequestRepository;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final TokenProvider tokenProvider;

    private final FriendRepository friendRepository;

    @Transactional
    public TodoListMessageResponse saveFriendRequest(String accessToken , long toUser) {
        long userId = tokenProvider.getUserId(accessToken);

        friendRequestRepository.save(FriendRequest.of(toUser , userId));

        return TodoListMessageResponse.of("요청이 성공적으로 수행되었습니다.");
    }

    @Transactional
    public void deleteFriendRequest(long requestId) {
        friendRequestRepository.deleteById(requestId);
    }

    public List<UserResponse> getFriendRequestList(String accessToken) {
        long userId = tokenProvider.getUserId(accessToken);

        return friendRequestRepository.findFriendRequestByUserId(userId);
    }

    public TodoListMessageResponse approveTheFriendRequest(long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new FriendException("해당 요청을 찾을 수 없습니다."));

        Friend friend = Friend.of(friendRequest.getToUserId() , friendRequest.getFromUserId());
        friendRepository.save(friend);

        return TodoListMessageResponse.of("요청이 성공적으로 수행되었습니다.");
    }

}
