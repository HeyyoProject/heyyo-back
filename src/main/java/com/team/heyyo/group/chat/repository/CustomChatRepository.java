package com.team.heyyo.group.chat.repository;

import com.team.heyyo.group.chat.dto.ChatResponse;

import java.util.List;

public interface CustomChatRepository {

    List<ChatResponse> findChatDataByMeetingId(long meetingId);

}
