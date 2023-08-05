package com.team.heyyo.mail.service;

import com.team.heyyo.mail.constant.MailCode;
import com.team.heyyo.mail.dto.MailMessage;
import com.team.heyyo.user.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailService {

  private final JavaMailSender javaMailSender;
  private final UserService userService;

  private SimpleMailMessage msg = new SimpleMailMessage();

  public MailCode sendSimpleMail(MailMessage mailMessage) {
    setMailMessage(mailMessage, mailMessage.subject(), mailMessage.text());
    return sendEmail();
  }


  @Transactional
  public MailCode sendTemporaryPassword(MailMessage mailMessage) {
    String temporaryPasswordSubject = "해요 임시 비밀번호 발급입니다.";
    String randomPassword = makeTemporaryPassword();

    userService.updatePassword(mailMessage.to(), randomPassword);
    setMailMessage(mailMessage, temporaryPasswordSubject, setFormToText(randomPassword));
    return sendEmail();
  }

  private void setMailMessage(MailMessage mailMessage, String subject, String text) {
    msg.setFrom(mailMessage.from());
    msg.setTo(mailMessage.to());
    msg.setSubject(subject);
    msg.setText(text);
  }

  private static String setFormToText(String text) {
    String frontTextForm = """
        안녕하세요 해요입니다. 
        임시 비밀번호를 발급해 드리겠습니다. 
        임시 비밀번호는 
        """;
    String lastTextForm = """
        입니다. 감사합니다.
        """;

    return frontTextForm + text + lastTextForm;
  }

  private MailCode sendEmail() {
    try {
      javaMailSender.send(msg);
      return MailCode.SUCCESS;
    } catch (MailException ex) {
      log.error(ex.getMessage());
      return MailCode.FAIL;
    }
  }

  private static String makeTemporaryPassword() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().substring(0, 8);
  }
}
