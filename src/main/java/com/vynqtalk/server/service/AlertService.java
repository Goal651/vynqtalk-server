package com.vynqtalk.server.service;

import com.vynqtalk.server.model.Alert;
import com.vynqtalk.server.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public void logAlert(String type, String message, String ipAddress) {
        Alert alert = new Alert(type, message, ipAddress);
        alertRepository.save(alert);
    }
}