package com.team.heyyo.user.repository;

import com.team.heyyo.config.TestConfig;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("ID로 사용자 비밀번호 변경")
    public void modifyPasswordWithId() {
        // given
        User user = userRepository.save(createUser());

        // when
        userRepository.updatePasswordWithId("newPassword" , user.getUserId());
        em.flush();
        em.clear();

        // then
        User user2 = userRepository.findById(user.getUserId()).get();
        assertThat(user2.getPassword()).isEqualTo("newPassword");
    }

    @Test
    @DisplayName("ID로 사용자 닉네임 변경")
    public void modifyNickNameWithId() {
        // given
        User user = userRepository.save(createUser());

        // when
        userRepository.updateNickNameWithId("newNickName" , user.getUserId());
        em.flush();
        em.clear();

        // then
        User user2 = userRepository.findById(user.getUserId()).get();
        assertThat(user2.getNickname()).isEqualTo("newNickName");
    }

    public User createUser() {
        return User.builder()
                .email("email")
                .name("name")
                .password("password")
                .nickname("nickNames")
                .role(UserRole.USER)
                .build();
    }

}
