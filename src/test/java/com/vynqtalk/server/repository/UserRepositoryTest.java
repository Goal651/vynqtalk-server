package com.vynqtalk.server.repository;

import com.vynqtalk.server.model.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setStatus("active");
        user.setBio("bio");
        user.setLastActive(java.time.Instant.now());
        user.setCreatedAt(java.time.Instant.now());
        user = userRepository.save(user);

        assertTrue(userRepository.findById(user.getId()).isPresent());
    }
} 