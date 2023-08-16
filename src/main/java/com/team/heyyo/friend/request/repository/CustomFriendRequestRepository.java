package com.team.heyyo.friend.request.repository;

import com.team.heyyo.friend.dto.UserResponse;

import java.util.List;

public interface CustomFriendRequestRepository {

    List<UserResponse> findFriendRequestByUserId(long userId);

}
