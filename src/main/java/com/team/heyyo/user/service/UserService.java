package com.team.heyyo.user.service;

import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * FIXME : 회원가입시 비밀번호 암호화 하는 방법 간단하게 구현 해놓았습니다.
     * @return
     */
    public Long save(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "test";

        return userRepository.save(
                User.builder()
                        .password(encoder.encode(password))
                        .build()
                )
                .getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }


}
