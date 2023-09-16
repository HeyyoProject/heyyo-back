package com.team.heyyo.group.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.heyyo.auth.jwt.support.TokenProvider;
import com.team.heyyo.group.chat.domain.Chat;
import com.team.heyyo.group.chat.dto.ChatRequest;
import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.MessageType;
import com.team.heyyo.group.chat.exception.ChatException;
import com.team.heyyo.group.chat.repository.ChatRepository;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.team.heyyo.group.chat.dto.ChatResponse.createChatResponse;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<Long , List<WebSocketSession>> sessionList = new HashMap<>();
    private final GroupStudyRepository groupStudyRepository;
    private final ChatRepository chatRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRequest chatMessage = objectMapper.readValue(message.getPayload(), ChatRequest.class);
        long groupStudyId = chatMessage.getGroupStudyId();

        if(chatMessage.getMessageType() == MessageType.ENTER) {
            joinChatBySession(groupStudyId , session);
            addParticipatesToGroupStudy(groupStudyId , chatMessage.getAccessToken() , session.getId());

        } else if(chatMessage.getMessageType() == MessageType.SEND) {
            sendChatToSameRootId(groupStudyId , objectMapper , chatMessage);
        }
    }

    private void joinChatBySession(long groupStudyId , WebSocketSession session) {
        if(!sessionList.containsKey(groupStudyId)) {
            sessionList.put(groupStudyId , new ArrayList<>());
        }
        List<WebSocketSession> sessions = sessionList.get(groupStudyId);
        if(!sessions.contains(session)) {
            sessions.add(session);
        }
    }

    private void addParticipatesToGroupStudy(long groupStudyId , String accessToken , String session) {
        User user = findUserByToken(accessToken);

        GroupStudy groupStudy = groupStudyRepository.findById(groupStudyId)
                .orElseThrow(() -> new ChatException("그룹 방을 찾을 수 없습니다."));

        groupStudy.addParticipants(user , session);
    }

    private void sendChatToSameRootId(long groupStudyId , ObjectMapper objectMapper , ChatRequest chatMessage) throws IOException {
        List<WebSocketSession> sessions = sessionList.get(groupStudyId);
        User user = findUserByToken(chatMessage.getAccessToken());

        for(WebSocketSession webSocketSession : sessions) {
            ChatResponse chatResponse = createChatResponse(chatMessage , user.getNickname());
            saveChatData(groupStudyId , chatResponse);

            String result = objectMapper.writeValueAsString(chatResponse);
            webSocketSession.sendMessage(new TextMessage(result));
        }
    }

    private void saveChatData(long meetingId , ChatResponse chatResponse) {
        GroupStudy groupStudy = groupStudyRepository.findGroupStudyAndChatById(meetingId)
                .orElseThrow(() -> new ChatException("모임 정보를 찾을 수 없습니다."));

        Chat chat = Chat.createChat(chatResponse , groupStudy);
        groupStudy.getChat().add(chat);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        for(Long groupStudyId : sessionList.keySet()) {
            List<WebSocketSession> webSocketSessions = sessionList.get(groupStudyId);

            for(int i = 0; i < webSocketSessions.size(); i++) {
                WebSocketSession socket = webSocketSessions.get(i);

                if(socket.getId().equals(session.getId())) {
                    chatRepository.deleteBySessionId(session.getId());
                    webSocketSessions.remove(i);
                    break;
                }
            }
        }
    }

    private User findUserByToken(String accessToken) {
        long userId = tokenProvider.getUserId(accessToken);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ChatException("사용자 정보를 찾을 수 없습니다."));

        return user;
    }

}
