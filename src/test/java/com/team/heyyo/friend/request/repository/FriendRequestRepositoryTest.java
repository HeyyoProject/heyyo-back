package com.team.heyyo.friend.request.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.request.entity.FriendRequest;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendRequestRepositoryTest {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("친구 목록 저장")
    public void saveFriend() {
        // given
        FriendRequest friend = buildFriend(1 , 2);

        // when
        FriendRequest result = friendRequestRepository.save(friend);

        // then
        assertThat(result.getFromUserId()).isEqualTo(1);
    }

    @Test
    @DisplayName("친구 목록 삭제 하기")
    public void deleteFriend() {
        // given
        FriendRequest friend = buildFriend(1 , 2);
        friendRequestRepository.save(friend);

        // when
        friendRequestRepository.deleteById(friend.getFriendRequestId());
        Optional<FriendRequest> result = friendRequestRepository.findById(friend.getFriendRequestId());

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("요청 목록 가져오기")
    public void getFriendRequestList() {
        // given
        User user1 = userRepository.save(new User( "email5"));
        User user2 = userRepository.save(new User("email6"));
        User user3 = userRepository.save(new User("email7"));
        User user4 = userRepository.save(new User("email8"));

        friendRequestRepository.save(buildFriend(user1.getUserId() , user2.getUserId()));
        friendRequestRepository.save(buildFriend(user1.getUserId() , user3.getUserId()));
        friendRequestRepository.save(buildFriend(user1.getUserId() , user4.getUserId()));
        friendRequestRepository.save(buildFriend(user2.getUserId() , user1.getUserId()));

        // when
        List<UserResponse> result = friendRequestRepository.findFriendRequestByUserId(user1.getUserId());

        // then
        assertThat(result.size()).isEqualTo(3);
    }

    public FriendRequest buildFriend(long fromUser , long toUser) {
        return FriendRequest.builder()
                .fromUserId(fromUser)
                .toUserId(toUser)
                .build();
    }

}
