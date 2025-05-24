package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    // Method to find a user by email
    User findByEmail(String email);
}
