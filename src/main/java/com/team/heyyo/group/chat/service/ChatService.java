package com.team.heyyo.group.chat.service;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public List<ChatResponse> findChatDataByStudyGroupId(long studyGroupId) {
        return chatRepository.findChatDataByMeetingId(studyGroupId);
    }

}