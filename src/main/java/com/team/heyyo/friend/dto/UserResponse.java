package com.team.heyyo.friend.dto;

import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.constant.UserCharacterType;
import com.team.heyyo.user.constant.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private UserCharacterType characterType;

}
