package com.team.heyyo.group.chat.service;

import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import com.team.heyyo.group.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantsService {

    private final ChatRepository chatRepository;

    public List<ParticipantsResponse> findParticipantsByStudyGroupId(long studyGroupId) {
        return chatRepository.findParticipantsByStudyGroupId(studyGroupId);
    }
}
