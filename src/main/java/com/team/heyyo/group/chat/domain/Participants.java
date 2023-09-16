package com.team.heyyo.group.chat.domain;

import com.team.heyyo.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Participants {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long participantsId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User participants;

    private String session;

}
