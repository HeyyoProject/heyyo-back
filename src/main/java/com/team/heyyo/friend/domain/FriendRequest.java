package com.team.heyyo.friend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "friend_request_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class FriendRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long friendRequestKey;

    private long toUserKey;

    private long fromUserKey;

}
