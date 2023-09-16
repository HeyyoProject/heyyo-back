package com.team.heyyo.group.chat.controller;

import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{studyGroupId}")
    public ResponseEntity findChatDataById(@PathVariable long studyGroupId) {
        List<ChatResponse> result = chatService.findChatDataByStudyGroupId(studyGroupId);

        return ResponseEntity.ok().body(result);
    }

}
