package com.vynqtalk.server.service;

import com.vynqtalk.server.model.SystemStatus;
import com.vynqtalk.server.repository.SystemStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SystemStatusService {
    @Autowired
    private SystemStatusRepository systemStatusRepository;

    public SystemStatus getStatus() {
        return systemStatusRepository.findAll().stream().findFirst().orElseGet(() -> {
            SystemStatus status = new SystemStatus();
            status.setInMaintenance(false);
            status.setMessage("System operational");
            status.setUpdatedAt(Instant.now());
            return systemStatusRepository.save(status);
        });
    }

    public SystemStatus updateStatus(boolean inMaintenance, String message) {
        SystemStatus status = getStatus();
        status.setInMaintenance(inMaintenance);
        status.setMessage(message);
        status.setUpdatedAt(Instant.now());
        return systemStatusRepository.save(status);
    }
} 