package com.team.heyyo.friend.list.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "friend_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Friend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    private Long toUserId;

    private Long fromUserId;

    public static Friend of(long toUser , long fromUser) {
        return Friend.builder()
                .toUserId(toUser)
                .fromUserId(fromUser)
                .build();
    }

}
