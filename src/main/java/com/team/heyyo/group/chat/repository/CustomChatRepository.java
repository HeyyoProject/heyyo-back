package com.team.heyyo.group.chat.repository;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import com.team.heyyo.user.dto.UserResponse;

import java.util.List;

public interface CustomChatRepository {

    List<ChatResponse> findChatDataByMeetingId(long meetingId);

    long deleteBySessionId(String session);

    List<ParticipantsResponse> findParticipantsByStudyGroupId(long studyGroupId);

}
