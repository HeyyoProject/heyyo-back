package com.team.heyyo.group.chat.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.group.chat.domain.Chat;
import com.team.heyyo.group.chat.dto.ChatResponse;
import com.team.heyyo.group.chat.dto.ParticipantsResponse;
import com.team.heyyo.group.study.domain.GroupStudy;
import com.team.heyyo.group.study.repository.groupstudy.GroupStudyRepository;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatRepositoryTest {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupStudyRepository groupStudyRepository;


    @Test
    @DisplayName("스터디 그룹에 참여하고 있는 사용자 탐색")
    public void findParticipantsByStudyGroupId () {
        // given
        GroupStudy groupStudy = GroupStudy.builder().build();
        User user1 = User.builder().email("email1").build();
        User user2 = User.builder().email("email2").build();

        userRepository.save(user1);
        userRepository.save(user2);
        groupStudy.addParticipants(user1 , "session1");
        groupStudy.addParticipants(user2 , "session2");
        GroupStudy result = groupStudyRepository.save(groupStudy);

        // when
        List<ParticipantsResponse> testResult =  chatRepository.findParticipantsByStudyGroupId(result.getGroupStudyId());

        // then
        assertThat(testResult.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("세션 ID로 참여중인 사용자 제거")
    public void deleteBySessionId() {
        // given
        GroupStudy groupStudy = GroupStudy.builder().build();
        User user1 = User.builder().email("email3").build();
        User user2 = User.builder().email("email4").build();

        userRepository.save(user1);
        userRepository.save(user2);
        groupStudy.addParticipants(user1 , "session3");
        groupStudy.addParticipants(user2 , "session4");
        GroupStudy result = groupStudyRepository.save(groupStudy);

        chatRepository.deleteBySessionId("session3");

        // when
        List<ParticipantsResponse> testResult = chatRepository.findParticipantsByStudyGroupId(result.getGroupStudyId());

        // then
        assertThat(testResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("채팅 데이터 가져오기")
    public void findChatDataByGroupStudyId() {
        // given
        GroupStudy groupStudy = GroupStudy.builder().build();
        groupStudy.addChatData(Chat.builder().build());
        groupStudy.addChatData(Chat.builder().build());
        groupStudy.addChatData(Chat.builder().build());
        GroupStudy groupStudyData = groupStudyRepository.save(groupStudy);

        GroupStudy groupStudy2 = GroupStudy.builder().build();
        groupStudy2.addChatData(Chat.builder().build());
        groupStudy2.addChatData(Chat.builder().build());
        groupStudyRepository.save(groupStudy2);

        // when
        List<ChatResponse> result = chatRepository.findChatDataByGroupStudyId(groupStudyData.getGroupStudyId());

        // then
        assertThat(result.size()).isEqualTo(3);
    }

}
