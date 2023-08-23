package com.team.heyyo.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.constant.UserCharacterType;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String name;
    private String password;
    private String phone;
    private Mbti mbtiType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD", timezone = "Asia/Seoul")
    private Date birth;
    private Boolean isMarketingAgree;
    private UserCharacterType characterType;

    public static UserResponse createUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .password(user.getPassword())
                .password(user.getPassword())
                .phone(user.getPhone())
                .mbtiType(user.getMbtiType())
                .birth(user.getBirth())
                .isMarketingAgree(user.getIsMarketingAgree())
                .characterType(user.getCharacterType())
                .build();
    }
}
