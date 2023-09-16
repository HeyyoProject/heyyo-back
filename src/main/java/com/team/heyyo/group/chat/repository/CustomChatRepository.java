package com.team.heyyo.group.chat.repository;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomChatRepository {

    List<ChatResponse> findChatDataByGroupStudyId(long groupStudyId);

    @Transactional
    long deleteBySessionId(String session);

    List<ParticipantsResponse> findParticipantsByStudyGroupId(long studyGroupId);

}
