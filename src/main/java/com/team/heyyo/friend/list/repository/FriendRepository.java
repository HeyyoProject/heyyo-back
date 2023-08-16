package com.team.heyyo.friend.list.repository;

import com.team.heyyo.friend.list.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> , CustomFriendRepository {

}
