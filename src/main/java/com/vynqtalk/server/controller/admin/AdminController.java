package com.vynqtalk.server.controller.admin;

import com.vynqtalk.server.dto.admin.SystemMetric;
import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.dto.user.UserUpdateRequest;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.service.admin.AdminService;
import com.vynqtalk.server.service.admin.SystemMetricsService;
import com.vynqtalk.server.service.system.SystemStatusService;
import com.vynqtalk.server.service.user.UserService;
import com.vynqtalk.server.model.system.Alert;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.dto.response.ChartData;
import java.time.Instant;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controller for admin-related endpoints, including user, group, alert, and
 * system status management.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AdminService adminService;
    private final SystemMetricsService systemMetricsService;

    public AdminController(UserService userService, UserMapper userMapper, AdminService adminService,
            SystemMetricsService systemMetricsService, SystemStatusService systemStatusService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.adminService = adminService;
        this.systemMetricsService = systemMetricsService;
    }

    /**
     * Returns system metrics for the dashboard.
     */
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<List<SystemMetric>>> getSystemData() {
        List<SystemMetric> systemMetric = systemMetricsService.getSystemMetrics();
        return ResponseEntity.ok(new ApiResponse<>(true,systemMetric, "System data loaded"));
    }

    /**
     * Returns all users as DTOs.
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true,adminService.getAllUsers(), "Users fetched successfully"));
    }

    /**
     * Returns all groups as DTOs.
     */
    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups() {
        return ResponseEntity.ok(new ApiResponse<>(true,adminService.getAllGroups(), "Groups fetched successfully"));
    }

    /**
     * Returns all alerts.
     */
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<Alert>>> getAllAlerts() {
        List<Alert> alerts = adminService.getAllAlerts();
        return ResponseEntity.ok(new ApiResponse<>(true,alerts, "Alerts fetched successfully"));
    }

    /**
     * Returns the most recent alerts, limited by the given number.
     */
    @GetMapping("/alerts/recent")
    public ResponseEntity<ApiResponse<List<Alert>>> getRecentAlerts(@RequestParam(defaultValue = "10") int limit) {
        List<Alert> alerts = adminService.getRecentAlerts(limit);
        return ResponseEntity.ok(new ApiResponse<>(true,alerts, "Recent alerts fetched"));
    }

    /**
     * Updates a user by ID using a UserUpdateRequest DTO.
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.getUserById(id);
        user.setStatus(userUpdateRequest.getStatus());
        userService.updateUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true,null, "User updated successfully"));
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
        double percentChange = messagesYesterday == 0 ? 100.0
                : ((double) (messagesToday - messagesYesterday) / messagesYesterday) * 100.0;
        stats.put("totalUsers", totalUsers);
        stats.put("newUsersThisMonth", newUsersThisMonth);
        stats.put("totalGroups", totalGroups);
        stats.put("newGroupsThisWeek", newGroupsThisWeek);
        stats.put("messagesToday", messagesToday);
        stats.put("messagesYesterday", messagesYesterday);
        stats.put("messagesPercentChange", percentChange);
        return ResponseEntity.ok(new ApiResponse<>(true,stats, "Dashboard stats loaded"));
    }

    @GetMapping("analytics")
    public ResponseEntity<ApiResponse<List<ChartData>>> getChartData(@RequestParam(required = false) Long start, @RequestParam(required = false) Long end) {
        Instant startInstant = start != null ? Instant.ofEpochMilli(start) : Instant.EPOCH;
        Instant endInstant = end != null ? Instant.ofEpochMilli(end) : Instant.now();
        List<ChartData> chartData = adminService.getChartData(startInstant, endInstant);
        return ResponseEntity.ok(new ApiResponse<>(true,chartData, "Chart data loaded"));
    }

}
