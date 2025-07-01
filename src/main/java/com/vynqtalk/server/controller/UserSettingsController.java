package com.vynqtalk.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.users.UserSettings;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.service.UserSettingsService;
import jakarta.validation.Valid;
import com.vynqtalk.server.dto.user.UserSettingsUpdateRequest;
import com.vynqtalk.server.dto.user.UserSettingsDTO;
import com.vynqtalk.server.mapper.UserSettingsMapper;
import com.vynqtalk.server.error.UserNotFoundException;
import com.vynqtalk.server.error.UserSettingsNotFoundException;

@RestController
@RequestMapping("/api/v1/user_settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;
    private final UserService userService;
    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsController(UserSettingsService userSettingsService, UserService userService, UserSettingsMapper userSettingsMapper) {
        this.userSettingsService = userSettingsService;
        this.userService = userService;
        this.userSettingsMapper = userSettingsMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> getUserSettings(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        UserSettings settings = userSettingsService.getUserSettings(user);
        if (settings == null) {
            throw new UserSettingsNotFoundException("User settings not found for user id: " + userId);
        }
        UserSettingsDTO dto = userSettingsMapper.toDTO(settings);
        return ResponseEntity.ok(new ApiResponse<>(dto, "User settings retrieved successfully", 200));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> updateUserSettings(
            @PathVariable Long userId,
            @Valid @RequestBody UserSettingsUpdateRequest updatedSettings) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        UserSettings settings = userSettingsService.updateUserSettings(user, updatedSettings);
        if (settings == null) {
            throw new UserSettingsNotFoundException("User settings not found for user id: " + userId);
        }
        UserSettingsDTO dto = userSettingsMapper.toDTO(settings);
        return ResponseEntity.ok(new ApiResponse<>(dto, "User settings updated successfully", 200));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserSettings(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        userSettingsService.deleteUserSettings(user);
        return ResponseEntity.ok(new ApiResponse<>(null, "User settings deleted successfully", 200));
    }
} 