package com.team.heyyo.friend.request.repository;

import com.team.heyyo.friend.request.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> , CustomFriendRequestRepository {

}
