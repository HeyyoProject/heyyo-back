package com.team.heyyo.group.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.group.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import static com.team.heyyo.group.study.domain.QGroupStudy.groupStudy;
import static com.team.heyyo.group.chat.domain.QChat.chat;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements CustomChatRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatResponse> findChatDataByMeetingId(long groupStudyId) {
        return jpaQueryFactory.select(Projections.constructor(
                        ChatResponse.class,
                        chat.chatId,
                        chat.sender,
                        chat.message,
                        chat.sendTime,
                        chat.senderImage
                )).from(chat)
                .innerJoin(chat.groupStudy , groupStudy).on(groupStudy.groupStudyId.eq(groupStudyId))
                .orderBy(chat.chatId.desc())
                .fetch();
    }
}
