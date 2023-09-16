package com.team.heyyo.group.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import com.team.heyyo.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team.heyyo.user.domain.QUser.user;
import static com.team.heyyo.group.chat.domain.QParticipants.participants1;
import static com.team.heyyo.group.study.domain.QGroupStudy.groupStudy;
import static com.team.heyyo.group.chat.domain.QChat.chat;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements CustomChatRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatResponse> findChatDataByGroupStudyId(long groupStudyId) {
        return jpaQueryFactory.select(Projections.constructor(
                        ChatResponse.class,
                        chat.chatId,
                        chat.sender,
                        chat.message,
                        chat.sendTime
                )).from(chat)
                .innerJoin(chat.groupStudy, groupStudy).on(groupStudy.groupStudyId.eq(groupStudyId))
                .orderBy(chat.chatId.desc())
                .fetch();
    }

    @Override
    public long deleteBySessionId(String session) {
        return jpaQueryFactory.delete(participants1)
                .where(participants1.session.eq(session))
                .execute();
    }

    @Override
    public List<ParticipantsResponse> findParticipantsByStudyGroupId(long studyGroupId) {
        return jpaQueryFactory.select(Projections.constructor(
                        ParticipantsResponse.class,
                        user.userId,
                        user.email,
                        user.nickname
                )).from(groupStudy)
                .innerJoin(groupStudy.participants, participants1)
                .innerJoin(participants1.participants, user)
                .where(groupStudy.groupStudyId.eq(studyGroupId))
                .fetch();
    }
}
