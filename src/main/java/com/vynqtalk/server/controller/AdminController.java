package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.dto.SystemMetric;
import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.UserDTO;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SystemMetricsService systemMetricsService;

    @Autowired
    private SystemStatusService systemStatusService;

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<List<SystemMetric>>> getSystemData() {
        List<SystemMetric> systemMetric = systemMetricsService.getSystemMetrics();
        return ResponseEntity.ok(new ApiResponse<>(systemMetric, "System data loaded", 200));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(adminService.getAllUsers(), "Users fetched successfully", 200));
    }

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups() {
        return ResponseEntity
                .ok(new ApiResponse<List<GroupDTO>>(adminService.getAllGroups(), "Groups fetched successfully", 200));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<Alert>>> getAllAlerts() {
        List<Alert> alerts = adminService.getAllAlerts();
        return ResponseEntity.ok(new ApiResponse<>(alerts, "Alerts fetched successfully", 200));
    }

    @GetMapping("/alerts/recent")
    public ResponseEntity<ApiResponse<List<Alert>>> getRecentAlerts(@RequestParam(defaultValue = "10") int limit) {
        List<Alert> alerts = adminService.getRecentAlerts(limit);
        return ResponseEntity.ok(new ApiResponse<>(alerts, "Recent alerts fetched", 200));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User response = userService.updateUser(userDTO);
        return ResponseEntity
                .ok(new ApiResponse<>(userMapper.toDTO(response), "User updated successfully", 200));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", 200));
    }

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

    @GetMapping("/system-status")
    public ResponseEntity<ApiResponse<SystemStatus>> getSystemStatus() {
        SystemStatus status = systemStatusService.getStatus();
        return ResponseEntity.ok(new ApiResponse<>(status, "System status fetched", 200));
    }

    @PutMapping("/system-status")
    public ResponseEntity<ApiResponse<SystemStatus>> updateSystemStatus(@RequestParam boolean inMaintenance, @RequestParam String message) {
        SystemStatus status = systemStatusService.updateStatus(inMaintenance, message);
        return ResponseEntity.ok(new ApiResponse<>(status, "System status updated", 200));
    }
}
