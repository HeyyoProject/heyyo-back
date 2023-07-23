package com.team.heyyo.user.service;

import com.team.heyyo.user.constant.UserRole;
import com.team.heyyo.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void saveTest() {
        //given
        //when
        Long id = userService.save();
        System.out.println(userService.findById(id).getPassword());

        //then

    }

}