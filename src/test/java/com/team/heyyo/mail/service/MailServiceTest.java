package com.team.heyyo.mail.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.team.heyyo.config.QueryDslConfig;
import com.team.heyyo.config.TestConfig;
import com.team.heyyo.mail.constant.MailCode;
import com.team.heyyo.mail.dto.MailMessage;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Import(QueryDslConfig.class)
class MailServiceTest {

  @Autowired
  MailService mailService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @DisplayName("매일 성공적으로 보내면 SUCCESS를 반환")
  @Test
  void simpleMailTest() {
    //given
    MailMessage mailMessage = MailMessage.of(
        "pica23000@naver.com",
        "테스트",
        "테스트"
    );

    //when
    MailCode mailCode = mailService.sendSimpleMail(mailMessage);

    //then
    assertThat(mailCode).isEqualTo(MailCode.SUCCESS);
  }

  @DisplayName("임시 비밀번호를 메일로 보내고 SUCCESS 반환")
  @Test
  void sendTemporaryPassword() {
    //given
    String email = "pica23000@naver.com";
    String oldPassword = "testPassword";
    userRepository.save(User.builder()
        .email(email)
        .password(oldPassword)
        .name("박재완")
        .build());

    MailMessage mailMessage = MailMessage.of(email);

    MailCode mailCode = mailService.sendTemporaryPassword(mailMessage);
    String newPassword = userRepository.findByEmail(email).get().getPassword();

    //then
    assertThat(mailCode).isEqualTo(MailCode.SUCCESS);
    assertThat(oldPassword).isNotEqualTo(newPassword);
  }

}