package com.vynqtalk.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.model.system.SystemStatus;
import com.vynqtalk.server.service.SystemStatusService;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemStatusService systemStatusService;

    public SystemController(SystemStatusService systemStatusService) {
        this.systemStatusService = systemStatusService;
    }

    /**
     * Gets the current system maintenance status.
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<SystemStatus>> getSystemStatus() {
        SystemStatus status = systemStatusService.getStatus();
        return ResponseEntity.ok(new ApiResponse<>(true,status, "System status fetched"));
    }

    /**
     * Updates the system maintenance status and message.
     */
    @PutMapping("/status")
    public ResponseEntity<ApiResponse<SystemStatus>> updateSystemStatus(@RequestParam boolean inMaintenance,
            @RequestParam String message) {
        SystemStatus status = systemStatusService.updateStatus(inMaintenance, message);
        return ResponseEntity.ok(new ApiResponse<>(true,status, "System status updated"));
    }
}
