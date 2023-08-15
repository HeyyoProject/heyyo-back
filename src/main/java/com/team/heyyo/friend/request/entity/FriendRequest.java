package com.team.heyyo.friend.request.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "friend_request_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class FriendRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestId;

    private Long toUserId;

    private Long fromUserId;

    public static FriendRequest of(long toUser , long fromUser) {
        return FriendRequest.builder()
                .fromUserId(fromUser)
                .toUserId(toUser)
                .build();
    }

}
