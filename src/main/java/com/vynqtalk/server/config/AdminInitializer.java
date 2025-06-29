package com.vynqtalk.server.config;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        String adminEmail = "bugiriwilson651@gmail.com";
        String adminPassword = "bugiri";
        String adminName = "wigo";

        if (userService.getUserByEmail(adminEmail) == null) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(adminPassword); // Will be hashed by saveUser
            admin.setName(adminName);
            admin.setIsAdmin(true);
            admin.setStatus("active");
            admin.setLastActive(Instant.now());
            admin.setCreatedAt(Instant.now());
            admin.setBio("Auto-created admin");

            userService.saveUser(admin);
            System.out.println("Admin user created: " + adminEmail);
        }
    }
}