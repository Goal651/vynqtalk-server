package com.vynqtalk.server.config.system;

import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class AdminInitializer {

    @Autowired
    private UserService userService;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name}")
    private String adminName;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void createAdminIfNotExists() {
        if (!userService.checkUserByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(adminPassword);
            admin.setName(adminName);
            admin.setUserRole(UserRole.ADMIN);
            admin.setStatus("active");
            admin.setLastActive(Instant.now());
            admin.setCreatedAt(Instant.now());
            admin.setBio("Auto-created admin");

            userService.saveUser(admin);
        } 
    }
}