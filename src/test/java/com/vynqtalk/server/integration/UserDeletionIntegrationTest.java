package com.vynqtalk.server.integration;

import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.UserRepository;
import com.vynqtalk.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserDeletionIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void deleteUser_removesUser() {
        User user = new User();
        user.setName("Delete Me");
        user.setEmail("deleteme@example.com");
        user.setPassword("password");
        user.setStatus("active");
        user.setBio("bio");
        user.setLastActive(java.time.Instant.now());
        user.setCreatedAt(java.time.Instant.now());
        user = userRepository.save(user);

        userService.deleteUser(user.getId());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
} 