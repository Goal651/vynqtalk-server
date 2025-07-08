package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.request.UserCreateRequest;
import com.vynqtalk.server.dto.request.UserSettingsUpdateRequest;
import com.vynqtalk.server.dto.request.UserUpdateRequest;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.ExportUserDTO;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.dto.user.UserSettingsDTO;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.mapper.UserSettingsMapper;
import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.users.UserSettings;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.service.UserSettingsService;

import jakarta.validation.Valid;

import com.vynqtalk.server.error.UserNotFoundException;
import com.vynqtalk.server.error.UserSettingsNotFoundException;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserSettingsService userSettingsService;
    private final UserSettingsMapper userSettingsMapper;

    public UserController(UserService userService, UserMapper userMapper, UserSettingsService userSettingsService,
            UserSettingsMapper userSettingsMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userSettingsService = userSettingsService;
        this.userSettingsMapper = userSettingsMapper;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserCreateRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setPassword(userRequest.getPassword());
        user.setUserRole(UserRole.USER);
        User result = userService.saveUser(user);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(userMapper.toDTO(result), "User created successfully", 201));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(user), "User retrieved successfully", 200));
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<ExportUserDTO>> getUserData(Principal principal) {
        ExportUserDTO userDTO = new ExportUserDTO();
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + principal.getName()));
        userDTO.setUser(userService.getUserWithUnreadMessages(user));
        return ResponseEntity.ok(new ApiResponse<>(userDTO, "Data processed successfully", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserDTO>> getUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + principal.getName()));
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(user), "Data processed successfully", 200));
    }

    // Update user profile
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateUser(Principal principal,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + principal.getName()));
        user.setName(userUpdateRequest.getName());
        user.setBio(userUpdateRequest.getBio());
                 userService.updateUser(user);
        return ResponseEntity.ok(new ApiResponse<>( "User updated successfully", 200));
    }

    // Delete user account
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + principal.getName()));
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(null, "User deleted successfully", 200));
    }

    // List all users
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(Principal principal) {
        List<UserDTO> userDTO = userService.getAllUsersWithLatestMessage();
        return ResponseEntity.ok(
                new ApiResponse<>(userDTO, userDTO.isEmpty() ? "No users found" : "Users retrieved successfully", 200));
    }

    // Get user settings
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> getUserSettings(Principal principal) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + principal.getName()));
        UserSettings settings = userSettingsService.getUserSettings(user);
        if (settings == null) {
            throw new UserSettingsNotFoundException("User settings not found for user id: " + principal.getName());
        }
        UserSettingsDTO dto = userSettingsMapper.toDTO(settings);
        return ResponseEntity.ok(new ApiResponse<>(dto, "User settings retrieved successfully", 200));
    }

    // Update user settings
    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> updateUserSettings(Principal principal,
            @RequestBody UserSettingsUpdateRequest updatedSettings) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + principal.getName()));
        UserSettings settings = userSettingsService.updateUserSettings(user, updatedSettings);
        if (settings == null) {
            throw new UserSettingsNotFoundException("User settings not found for user id: " + principal.getName());
        }
        UserSettingsDTO dto = userSettingsMapper.toDTO(settings);
        return ResponseEntity.ok(new ApiResponse<>(dto, "User settings updated successfully", 200));
    }

}
