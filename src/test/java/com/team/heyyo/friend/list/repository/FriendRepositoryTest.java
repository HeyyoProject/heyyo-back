package com.team.heyyo.friend.list.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.list.domain.Friend;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendRepositoryTest {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("친구 목록 저장")
    public void saveFriend() {
        // given
        Friend friend = buildFriend(1 , 2);

        // when
        Friend result = friendRepository.save(friend);

        // then
        assertThat(result.getFromUserId()).isEqualTo(1);
    }

    @Test
    @DisplayName("친구 삭제 하기")
    public void deleteFriend() {
        // given
        Friend friend = buildFriend(1 , 2);
        friendRepository.save(friend);

        // when
        friendRepository.deleteById(friend.getFriendId());
        Optional<Friend> result = friendRepository.findById(friend.getFriendId());

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("친구 정보 가져오기")
    public void getFriendListData() {
        // given
        User user1 = userRepository.save(new User( "email5"));
        User user2 = userRepository.save(new User("email6"));
        User user3 = userRepository.save(new User("email7"));
        User user4 = userRepository.save(new User("email8"));

        friendRepository.save(buildFriend(user1.getUserId() , user2.getUserId()));
        friendRepository.save(buildFriend(user1.getUserId() , user3.getUserId()));
        friendRepository.save(buildFriend(user1.getUserId() , user4.getUserId()));
        friendRepository.save(buildFriend(user2.getUserId() , user1.getUserId()));

        // when
        List<UserResponse> result = friendRepository.findFriendByUserId(user1.getUserId());

        // then
        assertThat(result.size()).isEqualTo(3);
    }

    public Friend buildFriend(long fromUser , long toUser) {
        return Friend.builder()
                .fromUserId(fromUser)
                .toUserId(toUser)
                .build();
    }
}
