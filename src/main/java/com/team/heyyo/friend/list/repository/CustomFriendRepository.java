package com.team.heyyo.friend.list.repository;

import com.team.heyyo.friend.dto.UserResponse;

import java.util.List;

public interface CustomFriendRepository {

    List<UserResponse> findFriendByUserId(long userId);

}
