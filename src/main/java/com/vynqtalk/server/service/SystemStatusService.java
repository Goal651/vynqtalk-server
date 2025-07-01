package com.vynqtalk.server.service;

import com.vynqtalk.server.model.system.SystemStatus;
import com.vynqtalk.server.repository.SystemStatusRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Service for managing system maintenance status.
 */
@Service
public class SystemStatusService {
    private final SystemStatusRepository systemStatusRepository;

    public SystemStatusService(SystemStatusRepository systemStatusRepository) {
        this.systemStatusRepository = systemStatusRepository;
    }

    /**
     * Gets the current system status, creating a default if none exists.
     */
    public SystemStatus getStatus() {
        return systemStatusRepository.findAll().stream().findFirst().orElseGet(() -> {
            SystemStatus status = new SystemStatus();
            status.setInMaintenance(false);
            status.setMessage("System operational");
            status.setUpdatedAt(Instant.now());
            return systemStatusRepository.save(status);
        });
    }

    /**
     * Updates the system maintenance status and message.
     */
    public SystemStatus updateStatus(boolean inMaintenance, String message) {
        SystemStatus status = getStatus();
        status.setInMaintenance(inMaintenance);
        status.setMessage(message);
        status.setUpdatedAt(Instant.now());
        return systemStatusRepository.save(status);
    }
} 