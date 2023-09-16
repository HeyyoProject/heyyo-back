package com.team.heyyo.group.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsResponse {
    private Long userId;
    private String email;
    private String nickname;
}
