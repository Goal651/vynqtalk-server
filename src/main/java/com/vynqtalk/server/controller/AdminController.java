package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.dto.SystemMetric;
import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.dto.user.UserUpdateRequest;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.service.AdminService;
import com.vynqtalk.server.service.SystemMetricsService;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.model.Alert;
import com.vynqtalk.server.model.SystemStatus;
import com.vynqtalk.server.service.SystemStatusService;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controller for admin-related endpoints, including user, group, alert, and system status management.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AdminService adminService;
    private final SystemMetricsService systemMetricsService;
    private final SystemStatusService systemStatusService;

    public AdminController(UserService userService, UserMapper userMapper, AdminService adminService,
                          SystemMetricsService systemMetricsService, SystemStatusService systemStatusService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.adminService = adminService;
        this.systemMetricsService = systemMetricsService;
        this.systemStatusService = systemStatusService;
    }

    /**
     * Returns system metrics for the dashboard.
     */
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<List<SystemMetric>>> getSystemData() {
        List<SystemMetric> systemMetric = systemMetricsService.getSystemMetrics();
        return ResponseEntity.ok(new ApiResponse<>(systemMetric, "System data loaded", 200));
    }

    /**
     * Returns all users as DTOs.
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(adminService.getAllUsers(), "Users fetched successfully", 200));
    }

    /**
     * Returns all groups as DTOs.
     */
    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups() {
        return ResponseEntity.ok(new ApiResponse<>(adminService.getAllGroups(), "Groups fetched successfully", 200));
    }

    /**
     * Returns all alerts.
     */
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<Alert>>> getAllAlerts() {
        List<Alert> alerts = adminService.getAllAlerts();
        return ResponseEntity.ok(new ApiResponse<>(alerts, "Alerts fetched successfully", 200));
    }

    /**
     * Returns the most recent alerts, limited by the given number.
     */
    @GetMapping("/alerts/recent")
    public ResponseEntity<ApiResponse<List<Alert>>> getRecentAlerts(@RequestParam(defaultValue = "10") int limit) {
        List<Alert> alerts = adminService.getRecentAlerts(limit);
        return ResponseEntity.ok(new ApiResponse<>(alerts, "Recent alerts fetched", 200));
    }

    /**
     * Updates a user by ID using a UserUpdateRequest DTO.
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new com.vynqtalk.server.error.UserNotFoundException("User not found with id: " + id));
        user.setEmail(userUpdateRequest.getEmail());
        user.setIsAdmin(userUpdateRequest.getIsAdmin());
        user.setName(userUpdateRequest.getName());
        user.setStatus(userUpdateRequest.getStatus());
        user.setLastActive(userUpdateRequest.getLastActive());
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(updatedUser), "User updated successfully", 200));
    }

    /**
     * Deletes a user by ID.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "User deleted successfully", 200));
    }

    /**
     * Returns dashboard statistics for the admin panel.
     */
    @GetMapping("/dashboard-stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        long totalUsers = adminService.getTotalUsers();
        long newUsersThisMonth = adminService.getNewUsersThisMonth();
        long totalGroups = adminService.getTotalGroups();
        long newGroupsThisWeek = adminService.getNewGroupsThisWeek();
        long messagesToday = adminService.getMessagesToday();
        long messagesYesterday = adminService.getMessagesYesterday();
        double percentChange = messagesYesterday == 0 ? 100.0 : ((double)(messagesToday - messagesYesterday) / messagesYesterday) * 100.0;
        stats.put("totalUsers", totalUsers);
        stats.put("newUsersThisMonth", newUsersThisMonth);
        stats.put("totalGroups", totalGroups);
        stats.put("newGroupsThisWeek", newGroupsThisWeek);
        stats.put("messagesToday", messagesToday);
        stats.put("messagesYesterday", messagesYesterday);
        stats.put("messagesPercentChange", percentChange);
        return ResponseEntity.ok(new ApiResponse<>(stats, "Dashboard stats loaded", 200));
    }

    /**
     * Gets the current system maintenance status.
     */
    @GetMapping("/system-status")
    public ResponseEntity<ApiResponse<SystemStatus>> getSystemStatus() {
        SystemStatus status = systemStatusService.getStatus();
        return ResponseEntity.ok(new ApiResponse<>(status, "System status fetched", 200));
    }

    /**
     * Updates the system maintenance status and message.
     */
    @PutMapping("/system-status")
    public ResponseEntity<ApiResponse<SystemStatus>> updateSystemStatus(@RequestParam boolean inMaintenance, @RequestParam String message) {
        SystemStatus status = systemStatusService.updateStatus(inMaintenance, message);
        return ResponseEntity.ok(new ApiResponse<>(status, "System status updated", 200));
    }
}
