package com.vynqtalk.server.service;

import com.vynqtalk.server.error.UserNotFoundException;
import com.vynqtalk.server.model.enums.AlertType;
import com.vynqtalk.server.model.system.Alert;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.AlertRepository;
import com.vynqtalk.server.repository.UserRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Service for logging system alerts.
 */
@Service
public class AlertService {
    private final AlertRepository alertRepository;
    private final UserRepository userService;

    public AlertService(AlertRepository alertRepository, UserRepository userService) {
        this.alertRepository = alertRepository;
        this.userService = userService;

    }

    /**
     * Logs an alert with the given type, message, and IP address.
     */
    public void logAlert(AlertType type, String message, String ipAddress, String email) {
        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User " + email + " not found");
        }
        Alert alert = new Alert(type, message, ipAddress,user.get());
        alertRepository.save(alert);
    }
} 