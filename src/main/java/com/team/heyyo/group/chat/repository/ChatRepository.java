package com.team.heyyo.group.chat.repository;

import com.team.heyyo.group.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> , CustomChatRepository {
}
