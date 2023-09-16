package com.team.heyyo.group.chat.domain;

import com.team.heyyo.group.study.domain.GroupStudy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    private GroupStudy groupStudy;

    @OneToMany(cascade = { CascadeType.PERSIST , CascadeType.REMOVE } , orphanRemoval = true)
    private List<Chat> chatData;

    @UpdateTimestamp
    private LocalDate lastChat;

    public static ChatRoom createChatRoom(GroupStudy groupStudy) {
        return ChatRoom.builder()
                .groupStudy(groupStudy)
                .lastChat(LocalDate.now())
                .build();
    }

}
