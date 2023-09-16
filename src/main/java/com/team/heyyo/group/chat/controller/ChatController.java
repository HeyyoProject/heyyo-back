package com.team.heyyo.group.chat.controller;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import com.team.heyyo.group.chat.service.ChatService;
import com.team.heyyo.group.chat.service.ParticipantsService;
import com.team.heyyo.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final ParticipantsService participantsService;

    @GetMapping("/{studyGroupId}")
    public ResponseEntity findChatDataById(@PathVariable long studyGroupId) {
        List<ChatResponse> result = chatService.findChatDataByStudyGroupId(studyGroupId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/participants/{studyGroupId}")
    public ResponseEntity findParticipantsByStudyGroupId(@PathVariable long studyGroupId) {
        List<ParticipantsResponse> result = participantsService.findParticipantsByStudyGroupId(studyGroupId);

        return ResponseEntity.ok().body(result);
    }

}
