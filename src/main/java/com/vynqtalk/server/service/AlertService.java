package com.vynqtalk.server.service;

import com.vynqtalk.server.model.system.Alert;
import com.vynqtalk.server.repository.AlertRepository;
import org.springframework.stereotype.Service;

/**
 * Service for logging system alerts.
 */
@Service
public class AlertService {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Logs an alert with the given type, message, and IP address.
     */
    public void logAlert(String type, String message, String ipAddress) {
        Alert alert = new Alert(type, message, ipAddress);
        alertRepository.save(alert);
    }
}