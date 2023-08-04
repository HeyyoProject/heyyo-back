package com.team.heyyo.user.repository;

public interface UserRepositoryCustom {

  Long updatePasswordWithEmail(String password, String email);
}
