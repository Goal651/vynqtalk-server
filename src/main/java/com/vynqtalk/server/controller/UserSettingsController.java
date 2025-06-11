package com.vynqtalk.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.UserSettings;
import com.vynqtalk.server.model.response.ApiResponse;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.service.UserSettingsService;

@RestController
@RequestMapping("/api/v1/user_settings")
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserSettings>> getUserSettings(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserSettings settings = userSettingsService.getUserSettings(user);
        return ResponseEntity.ok(new ApiResponse<>(settings, "User settings retrieved successfully", 200));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserSettings>> updateUserSettings(
            @PathVariable Long userId,
            @RequestBody UserSettings updatedSettings) {
        User user = userService.getUserById(userId);
        UserSettings settings = userSettingsService.updateUserSettings(user, updatedSettings);
        return ResponseEntity.ok(new ApiResponse<>(settings, "User settings updated successfully", 200));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserSettings(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        userSettingsService.deleteUserSettings(user);
        return ResponseEntity.ok(new ApiResponse<>(null, "User settings deleted successfully", 200));
    }
} 