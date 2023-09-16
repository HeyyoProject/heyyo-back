package com.team.heyyo.group.study.domain;

import com.team.heyyo.group.chat.domain.Chat;
import com.team.heyyo.group.chat.domain.Participants;
import com.team.heyyo.user.constant.Mbti;
import com.team.heyyo.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Table(name = "group_study_tb")
@Getter
@Entity
public class GroupStudy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupStudyId;

    @Column(name = "owner_user_id")
    private Long userId;

    private String title;

    private String description;

    private String session;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(cascade = { CascadeType.PERSIST , CascadeType.REMOVE } , orphanRemoval = true)
    @Builder.Default
    private List<Chat> chat = new LinkedList<>();

    @OneToMany(cascade = { CascadeType.PERSIST , CascadeType.REMOVE } , orphanRemoval = true)
    @Builder.Default
    private List<Participants> participants = new LinkedList<>();

    @Builder
    public GroupStudy(Long userId, String title, String description, String session, Mbti mbti) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.session = session;
        this.mbti = mbti;
    }

    public void addParticipants(User user) {
        Participants data = Participants.builder().participants(user).build();

        participants.add(data);
    }

    protected GroupStudy() {

    }
}
