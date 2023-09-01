package com.team.heyyo.friend.dto;

import com.team.heyyo.user.constant.Mbti;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long userId;

    private String email;

    private String name;

    private String password;

    private String phone;

    private Mbti mbtiType;

    private Date birth;

    private String nickname;

}
