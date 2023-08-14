package com.team.heyyo.friend.list.service;

import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.list.domain.Friend;
import com.team.heyyo.friend.list.repository.FriendRepository;
import com.team.heyyo.todolist.dto.TodoListMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FriendService {

    private final FriendRepository friendRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public TodoListMessageResponse saveFriend(String accessToken , long toUser) {
        long userId = tokenProvider.getUserId(accessToken);

        friendRepository.save(Friend.of(toUser , userId));

        return TodoListMessageResponse.of("요청이 성공적으로 수행되었습니다.");
    }

    @Transactional
    public void deleteFriend(long friendId) {
        friendRepository.deleteById(friendId);
    }

    public List<UserResponse> getFriendList(String accessToken) {
        long userId = tokenProvider.getUserId(accessToken);

        return friendRepository.findFriendByUserId(userId);
    }

}
