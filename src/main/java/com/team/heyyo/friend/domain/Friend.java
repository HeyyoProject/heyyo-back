package com.team.heyyo.friend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "friend_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Friend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long friendId;

    private long toUserId;

    private long fromUserId;

}
