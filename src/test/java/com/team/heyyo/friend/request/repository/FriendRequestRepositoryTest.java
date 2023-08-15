package com.team.heyyo.friend.request.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.friend.dto.UserResponse;
import com.team.heyyo.friend.request.entity.FriendRequest;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
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
    @DisplayName("친구 목록 가져오기")
    public void getFriendList() {
        // given
        userRepository.save(new User(1 , "email1"));
        userRepository.save(new User(2 , "email2"));
        userRepository.save(new User(3 , "email3"));
        userRepository.save(new User(4 , "email4"));

        friendRequestRepository.save(buildFriend(1 , 2));
        friendRequestRepository.save(buildFriend(1 , 3));
        friendRequestRepository.save(buildFriend(1 , 4));
        friendRequestRepository.save(buildFriend(2 , 1));

        // when
        List<UserResponse> result = friendRequestRepository.findFriendRequestByUserId(1);

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
