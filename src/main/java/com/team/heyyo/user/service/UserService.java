package com.team.heyyo.user.service;

import com.team.heyyo.user.constant.UserResponseCode;
import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import com.team.heyyo.user.dto.UserRegisterRequest;
import com.team.heyyo.user.exception.UserNotFoundException;
import com.team.heyyo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean register(UserRegisterRequest request) {
        System.out.println(request.getPassword());
        userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .role(UserRole.USER)
                        .build()
        );
        return true;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 id와 일치하는 user가 없습니다."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 email과 일치하는 user가 없습니다."));
    }


    public UserResponseCode isEmailAndPasswordCorrect(String email, String requestPassword) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return UserResponseCode.EMAIL_NOT_FOUND;
        }

        User user = optionalUser.get();

        if (passwordEncoder.matches(requestPassword, user.getPassword())) {
            return UserResponseCode.SUCCESS;
        } else {
            return UserResponseCode.PASSWORD_NOT_MATCH;
        }
    }
}
