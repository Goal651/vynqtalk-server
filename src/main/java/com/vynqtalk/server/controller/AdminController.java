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

import java.util.List;

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
}
