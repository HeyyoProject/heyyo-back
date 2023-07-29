package com.team.heyyo.user.service;

import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.exception.UserNotFoundException;
import com.team.heyyo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * FIXME : 회원가입시 비밀번호 암호화 하는 방법 간단하게 구현 해놓았습니다.
     * @return
     */
    @Transactional
    public Long save(){

        String password = "test";

        return userRepository.save(
                User.builder()
                        .email("test@email.com")
                        .password(passwordEncoder.encode(password))
                        .build()
                )
                .getUserId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 id와 일치하는 user가 없습니다."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 email과 일치하는 user가 없습니다."));
    }


}
