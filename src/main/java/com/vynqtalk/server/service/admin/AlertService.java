package com.vynqtalk.server.service.admin;

import com.vynqtalk.server.exceptions.UserNotFoundException;
import com.vynqtalk.server.model.enums.AlertType;
import com.vynqtalk.server.model.system.Alert;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.AlertRepository;
import com.vynqtalk.server.repository.UserRepository;

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
        User user = userService.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        Alert alert = new Alert(type, message, ipAddress, user.getEmail());
        alertRepository.save(alert);
    }
}