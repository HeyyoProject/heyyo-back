package com.team.heyyo.user.repository;

import com.team.heyyo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  Optional<User> findByEmail(String email);

  Optional<User> findUserByEmailAndName(String email, String name);

}
