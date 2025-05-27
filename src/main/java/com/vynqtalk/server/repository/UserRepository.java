package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
